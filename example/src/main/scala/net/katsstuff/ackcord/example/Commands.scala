/*
 * This file is part of AkkaCord, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 Katrix
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.katsstuff.ackcord.example

import java.nio.file.Paths

import akka.NotUsed
import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import net.katsstuff.ackcord.DiscordClient
import net.katsstuff.ackcord.DiscordClient.{ClientActor, ShutdownClient}
import net.katsstuff.ackcord.commands._
import net.katsstuff.ackcord.data._
import net.katsstuff.ackcord.http.requests.RESTRequests.CreateMessageData
import net.katsstuff.ackcord.http.requests.{RequestFailed, RequestResponse, RequestWrapper, RESTRequests}
import net.katsstuff.ackcord.syntax._

class PingCmd(val client: ClientActor) extends ParsedCmdActor[NotUsed] {
  override def handleCommand(msg: Message, args: NotUsed, remaining: List[String])(implicit c: CacheSnapshot): Unit =
    client ! RequestWrapper(RESTRequests.CreateMessage(msg.channelId, CreateMessageData("Pong")), NotUsed, self)
}
object PingCmdFactory
    extends ParsedCmdFactory[NotUsed](
      category = ExampleCmdCategories.!,
      aliases = Seq("ping"),
      cmdProps = client => Props(new PingCmd(client)),
      description =
        Some(CmdDescription(name = "Ping", description = "Ping this bot and get a response. Used for testing"))
    )

class SendFileCmd(val client: ClientActor) extends ParsedCmdActor[NotUsed] {
  override def handleCommand(msg: Message, args: NotUsed, remaining: List[String])(implicit c: CacheSnapshot): Unit = {
    val embed = OutgoingEmbed(
      title = Some("This is an embed"),
      description = Some("This embed is sent together with a file"),
      fields = Seq(EmbedField("FileName", "theFile.txt"))
    )

    msg.tChannel.foreach { tChannel =>
      //Use channel to construct request
      client ! tChannel.sendMessage("Here is the file", files = Seq(Paths.get("theFile.txt")), embed = Some(embed))
    }
  }
}
object SendFileCmdFactory
    extends ParsedCmdFactory[NotUsed](
      category = ExampleCmdCategories.!,
      aliases = Seq("sendFile"),
      cmdProps = client => Props(new SendFileCmd(client)),
      description = Some(
        CmdDescription(name = "Send file", description = "Make the bot send an embed with a file. Used for testing")
      )
    )

class InfoChannelCmd(val client: ClientActor) extends ParsedCmdActor[GuildChannel] {
  import InfoChannelCmd._

  def infoResponseHandler: ActorRef = context.actorOf(InfoCommandHandler.props(client))

  override def handleCommand(msg: Message, channel: GuildChannel, remaining: List[String])(
      implicit c: CacheSnapshot
  ): Unit =
    client ! client.fetchChannel(channel.id, GetChannelInfo(channel.guildId, channel.id, msg.channelId, c))(
      infoResponseHandler
    )
}
object InfoChannelCmd {
  case class GetChannelInfo(
      guildId: GuildId,
      requestedChannelId: ChannelId,
      senderChannelId: ChannelId,
      c: CacheSnapshot
  )
}
object InfoChannelCmdFactory extends ParsedCmdFactory[GuildChannel](
  category = ExampleCmdCategories.!,
  aliases = Seq("infoChannel"),
  cmdProps = client => Props(new InfoChannelCmd(client)),
  description = Some(
    CmdDescription(
      name = "Channel info",
      description = "Make the bot fetch information about a text channel from Discord. Used for testing"
    )
  )
)

class InfoCommandHandler(client: ClientActor) extends Actor with ActorLogging {
  import InfoChannelCmd._
  override def receive: Receive = {
    case RequestResponse(res, GetChannelInfo(guildId, requestedChannelId, senderChannelId, c), _, _, _) =>
      implicit val cache: CacheSnapshot = c
      requestedChannelId.guildResolve(guildId).map(_.name) match {
        case Some(name) =>
          senderChannelId.guildResolve(guildId) match {
            case Some(channel: TGuildChannel) => client ! channel.sendMessage(s"Info for $name:\n$res")
            case Some(channel: GuildChannel)  => log.warning("{} is not a valid text channel", channel.name)
            case None                         => log.warning("No channel found for {}", requestedChannelId)
          }
        case None => log.warning("No channel found for {}", requestedChannelId)
      }

      log.info(res.toString)
      context.stop(self)
    case error: RequestFailed[_, _] =>
      val GetChannelInfo(guildId, _, senderChannelId, c) = error.context
      implicit val cache: CacheSnapshot = c
      senderChannelId.tResolve(guildId).foreach(client ! _.sendMessage("Error encountered"))
      context.stop(self)
  }
}
object InfoCommandHandler {
  def props(client: ClientActor): Props = Props(new InfoCommandHandler(client))
}

class KillCmd(main: ActorRef, val client: ClientActor) extends ParsedCmdActor[NotUsed] with ActorLogging {

  override def extraReceive: Receive = {
    case DiscordClient.ShutdownClient => //We make sure to ignore this to be able to run code after the shutdown is complete
    case Terminated(`main`) =>
      log.info("Everything shut down")
      context.system.terminate()
      context.system.terminate()
  }

  override def handleCommand(msg: Message, args: NotUsed, remaining: List[String])(implicit c: CacheSnapshot): Unit = {
    log.info("Received shutdown command")
    main ! ShutdownClient
    context.watch(main)
  }
}
class KillCmdFactory(main: ActorRef) extends ParsedCmdFactory[NotUsed](
  category = ExampleCmdCategories.!,
  aliases = Seq("kill", "die"),
  cmdProps = client => Props(new KillCmd(main, client)),
  description = Some(CmdDescription(name = "Kill bot", description = "Shut down this bot"))
)
object KillCmdFactory {
  def apply(main: ActorRef): KillCmdFactory = new KillCmdFactory(main)
}

class ExampleErrorHandler(val client: ClientActor, allCommands: Map[CmdCategory, Set[String]]) extends CmdErrorHandler {
  override def noCommandReply(msg: Message)(implicit c: CacheSnapshot): Option[CreateMessageData] =
    Some(CreateMessageData("No command specified"))
  override def unknownCommandReply(msg: Message, cat: CmdCategory, command: String, args: List[String])(
      implicit c: CacheSnapshot
  ): Option[CreateMessageData] =
    if (allCommands.get(cat).exists(_.contains(command))) None
    else Some(CreateMessageData(s"No command named $command known"))
}
object ExampleErrorHandler {
  def props(client: ClientActor, allCommands: Map[CmdCategory, Set[String]]): Props =
    Props(new ExampleErrorHandler(client, allCommands))
}

class IgnoreUnknownErrorHandler(val client: ClientActor) extends CmdErrorHandler {
  override def noCommandReply(msg: Message)(implicit c: CacheSnapshot): Option[CreateMessageData] = None
  override def unknownCommandReply(msg: Message, cat: CmdCategory, command: String, args: List[String])(
      implicit c: CacheSnapshot
  ): Option[CreateMessageData] = None
}
object IgnoreUnknownErrorHandler {
  def props(client: ClientActor): Props = Props(new IgnoreUnknownErrorHandler(client))
}
