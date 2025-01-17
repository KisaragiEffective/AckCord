/*
 * This file is part of AckCord, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2019 Katrix
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
package ackcord.data

import java.time.{Instant, OffsetDateTime}

import scala.util.Try

import ackcord.data.AuditLogChange.PartialRole
import ackcord.data.raw._
import cats.instances.either._
import cats.instances.option._
import cats.syntax.all._
import io.circe._
import io.circe.generic.extras.Configuration
import io.circe.syntax._

//noinspection NameBooleanParameters
trait DiscordProtocol {

  implicit val circeConfiguration: Configuration = Configuration.default.withSnakeCaseMemberNames.withDefaults

  implicit def snowflakeTypeCodec[A]: Codec[SnowflakeType[A]] = Codec.from(
    Decoder[String].emap(s => Right(SnowflakeType[A](s))),
    Encoder[String].contramap(_.asString)
  )

  implicit def snowflakeTypeKeyDecoder[A]: KeyDecoder[SnowflakeType[A]] =
    KeyDecoder.decodeKeyString.map(s => SnowflakeType[A](s))
  implicit def snowflakeTypeKeyEncoder[A]: KeyEncoder[SnowflakeType[A]] =
    KeyEncoder.encodeKeyString.contramap(_.asString)

  implicit val instantCodec: Codec[Instant] = Codec.from(
    Decoder[Long].emapTry(l => Try(Instant.ofEpochSecond(l))),
    Encoder[Long].contramap(_.getEpochSecond)
  )

  implicit val permissionCodec: Codec[Permission] = Codec.from(
    Decoder[BigInt].emap(i => Right(Permission.fromBigInt(i))),
    Encoder[BigInt].contramap(_.toBigInt)
  )

  implicit val userFlagsCodec: Codec[UserFlags] = Codec.from(
    Decoder[Int].emap(i => Right(UserFlags.fromInt(i))),
    Encoder[Int].contramap(_.toInt)
  )

  implicit val messageFlagsCodec: Codec[MessageFlags] = Codec.from(
    Decoder[Int].emap(i => Right(MessageFlags.fromInt(i))),
    Encoder[Int].contramap(_.toInt)
  )

  implicit val systemChannelFlagsCodec: Codec[SystemChannelFlags] = Codec.from(
    Decoder[Int].emap(i => Right(SystemChannelFlags.fromInt(i))),
    Encoder[Int].contramap(_.toInt)
  )

  implicit val applicationFlagsCodec: Codec[ApplicationFlags] = Codec.from(
    Decoder[Int].emap(i => Right(ApplicationFlags.fromInt(i))),
    Encoder[Int].contramap(_.toInt)
  )

  implicit val activityFlagsCodec: Codec[ActivityFlags] = Codec.from(
    Decoder[Int].emap(i => Right(ActivityFlags.fromInt(i))),
    Encoder[Int].contramap(_.toInt)
  )

  implicit val offsetDateTimeCodec: Codec[OffsetDateTime] = Codec.from(
    Decoder[String].emapTry(s => Try(OffsetDateTime.parse(s))),
    Encoder[String].contramap[OffsetDateTime](_.toString)
  )

  implicit val imageDataCodec: Codec[ImageData] = Codec.from(
    Decoder[String].emap(s => Right(new ImageData(s))),
    Encoder[String].contramap(_.rawData)
  )

  implicit val rawThreadMemberCodec: Codec[RawThreadMember] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val rawThreadMetadataCodec: Codec[RawThreadMetadata] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val rawChannelCodec: Codec[RawChannel] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val welcomeScreenChannelCodec: Codec[WelcomeScreenChannel] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val welcomeScreenCodec: Codec[WelcomeScreen] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val stageInstanceCodec: Codec[StageInstance] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val rawGuildCodec: Codec[RawGuild] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val rawGuildPreviewCodec: Codec[GuildPreview] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val partialUserCodec: Codec[PartialUser] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val rawActivityCodec: Codec[RawActivity] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val activityTimestampsCodec: Codec[ActivityTimestamps] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val activityAssetCodec: Codec[ActivityAsset] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val rawActivityPartyCodec: Codec[RawActivityParty] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val activityEmojiCodec: Codec[ActivityEmoji] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val activitySecretsCodec: Codec[ActivitySecrets] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val rawPresenceCodec: Codec[RawPresence] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val unavailableGuildCodec: Codec[UnavailableGuild] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val permissionValueCodec: Codec[PermissionOverwrite] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val userCodec: Codec[User] = derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val webhookAuthorCodec: Codec[WebhookAuthor] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val roleTagsCodec: Codec[RoleTags] = Codec.from(
    (c: HCursor) =>
      for {
        botId         <- c.get[Option[UserId]]("bot_id")
        integrationId <- c.get[Option[IntegrationId]]("integration_id")
      } yield RoleTags(botId, integrationId, c.downField("premium_subscriber").succeeded),
    (a: RoleTags) => {
      val base = Json.obj(
        "bot_id"         := a.botId,
        "integration_id" := a.integrationId
      )

      if (a.premiumSubscriber) base.withObject(o => Json.fromJsonObject(o.add("premium_subscriber", Json.Null)))
      else base
    }
  )

  implicit val roleCodec: Codec[Role] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None) //Encoding roles is fine, decoding them is not

  implicit val rawRoleCodec: Codec[RawRole] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val rawGuildMemberCodec: Codec[RawGuildMember] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val attachementCodec: Codec[Attachment] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val embedFieldCodec: Codec[EmbedField] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val receivedEmbedFooterCodec: Codec[ReceivedEmbedFooter] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val receivedEmbedImageCodec: Codec[ReceivedEmbedImage] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val receivedEmbedThumbnailCodec: Codec[ReceivedEmbedThumbnail] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val receivedEmbedVideoCodec: Codec[ReceivedEmbedVideo] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val receivedEmbedProviderCodec: Codec[ReceivedEmbedProvider] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val receivedEmbedAuthorCodec: Codec[ReceivedEmbedAuthor] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val receivedEmbedCodec: Codec[ReceivedEmbed] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val outgoingEmbedFooterCodec: Codec[OutgoingEmbedFooter] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val outgoingEmbedImageCodec: Codec[OutgoingEmbedImage] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val outgoingEmbedVideoCodec: Codec[OutgoingEmbedVideo] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val outgoingEmbedThumbnailCodec: Codec[OutgoingEmbedThumbnail] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val outgoingEmbedAuthorCodec: Codec[OutgoingEmbedAuthor] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val outgoingEmbedCodec: Codec[OutgoingEmbed] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val partialEmojiCodec: Codec[PartialEmoji] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val reactionCodec: Codec[Reaction] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val rawMessageActivityCodec: Codec[RawMessageActivity] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val partialRawGuildMemberCodec: Codec[PartialRawGuildMember] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val channelMentionCodec: Codec[ChannelMention] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val messageReferenceCodec: Codec[MessageReference] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val stickerCodec: Codec[Sticker] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val stickerItemCodec: Codec[StickerItem] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val messageInteractionCodec: Codec[MessageInteraction] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit private val rawButtonCodec: Codec[RawButton] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val buttonEncoder: Encoder[Button] = (a: Button) => {
    val rawButton = a match {
      case raw: RawButton => raw
      case _              => RawButton(a.label, a.customId, a.style, a.emoji, a.url, a.disabled)
    }
    rawButton.asJson.deepMerge(Json.obj("type" := a.tpe))
  }

  implicit val buttonDecoder: Decoder[Button] = (c: HCursor) => {
    c.as[RawButton].map { button =>
      val buttonValid = (button.label.isDefined || button.emoji.isDefined) && button.label
        .forall(_.length <= 80) && button.customId.forall(_.length <= 100)

      if (buttonValid) {
        val asTextButton = button.customId
          .map(TextButton(button.label, _, button.style.asInstanceOf[TextButtonStyle], button.emoji, button.disabled))

        val asLinkButton = button.url
          .filter(_ => button.style == ButtonStyle.Link)
          .map(LinkButton(button.label, button.emoji, _, button.disabled))

        asTextButton.orElse(asLinkButton).getOrElse(button)
      } else {
        button
      }
    }
  }

  implicit val selectOptionCodec: Codec[SelectOption] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val selectMenuEncoder: Encoder[SelectMenu] = {
    val base: Encoder[SelectMenu] = derivation.deriveEncoder(derivation.renaming.snakeCase, None)
    (a: SelectMenu) => base(a).deepMerge(Json.obj("type" := a.tpe))
  }

  implicit private val selectMenuDecoder: Decoder[SelectMenu] =
    derivation.deriveDecoder(derivation.renaming.snakeCase, false, None)

  implicit val actionRowContentCodec: Codec[ActionRowContent] = Codec.from(
    (c: HCursor) =>
      c.get[ComponentType]("type").flatMap {
        case ComponentType.Button      => c.as[Button]
        case ComponentType.SelectMenu  => c.as[SelectMenu]
        case ComponentType.ActionRow   => Left(DecodingFailure("Invalid component type ActionRow", c.history))
        case ComponentType.Unknown(id) => Left(DecodingFailure(s"Unknown component type $id", c.history))
      },
    {
      case button: Button   => button.asJson
      case menu: SelectMenu => menu.asJson
    }
  )

  implicit val actionRowCodec: Codec[ActionRow] = {
    val base: Codec[ActionRow] = derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

    Codec.from(base, base.mapJson(json => json.deepMerge(Json.obj("type" := 1))))
  }
  implicit val applicationCodec: Codec[Application] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val partialApplicationCodec: Codec[PartialApplication] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val rawMessageEncoder: Encoder[RawMessage] = (a: RawMessage) => {
    val base = Seq(
      "id"               -> a.id.asJson,
      "channel_id"       -> a.channelId.asJson,
      "content"          -> a.content.asJson,
      "timestamp"        -> a.timestamp.asJson,
      "edited_timestamp" -> a.editedTimestamp.asJson,
      "tts"              -> a.tts.asJson,
      "mention_everyone" -> a.mentionEveryone.asJson,
      "mentions"         -> a.mentions.asJson,
      "mention_roles"    -> a.mentionRoles.asJson,
      "attachments"      -> a.attachment.asJson,
      "embeds"           -> a.embeds.asJson,
      "reactions"        -> a.reactions.asJson,
      "nonce"            -> a.nonce.map(_.fold(_.asJson, _.asJson)).asJson,
      "pinned"           -> a.pinned.asJson,
      "type"             -> a.`type`.asJson,
      "activity"         -> a.activity.asJson,
      "application"      -> a.application.asJson,
      "components"       -> a.components.asJson
    )

    a.author match {
      case user: User => Json.obj(base :+ "author" -> user.asJson: _*)
      case webhook: WebhookAuthor =>
        Json.obj(base ++ Seq("author" -> webhook.asJson, "webhook_id" -> webhook.id.asJson): _*)
    }
  }
  implicit val rawMessageDecoder: Decoder[RawMessage] = (c: HCursor) => {
    val isWebhook = c.keys.exists(_.toSeq.contains("webhook_id"))

    for {
      id              <- c.get[MessageId]("id")
      channelId       <- c.get[TextChannelId]("channel_id")
      guildId         <- c.get[Option[GuildId]]("guild_id")
      author          <- if (isWebhook) c.get[WebhookAuthor]("author") else c.get[User]("author")
      member          <- c.get[Option[PartialRawGuildMember]]("member")
      content         <- c.get[String]("content")
      timestamp       <- c.get[OffsetDateTime]("timestamp")
      editedTimestamp <- c.get[Option[OffsetDateTime]]("edited_timestamp")
      tts             <- c.get[Boolean]("tts")
      mentionEveryone <- c.get[Boolean]("mention_everyone")
      mentions        <- c.get[Seq[User]]("mentions")
      mentionRoles    <- c.get[Seq[RoleId]]("mention_roles")
      mentionChannels <- c.get[Option[Seq[ChannelMention]]]("mention_channels")
      attachment      <- c.get[Seq[Attachment]]("attachments")
      embeds          <- c.get[Seq[ReceivedEmbed]]("embeds")
      reactions       <- c.get[Option[Seq[Reaction]]]("reactions")
      nonce <-
        c
          .get[Option[Long]]("nonce")
          .map(_.map(Left.apply))
          .orElse(c.get[Option[String]]("nonce").map(_.map(Right.apply)))
      pinned             <- c.get[Boolean]("pinned")
      tpe                <- c.get[MessageType]("type")
      activity           <- c.get[Option[RawMessageActivity]]("activity")
      application        <- c.get[Option[PartialApplication]]("application")
      applicationId      <- c.get[Option[ApplicationId]]("application_id")
      messageReference   <- c.get[Option[MessageReference]]("message_reference")
      flags              <- c.get[Option[MessageFlags]]("flags")
      stickers           <- c.get[Option[Seq[Sticker]]]("stickers")
      stickerItems       <- c.get[Option[Seq[StickerItem]]]("sticker_items")
      referencedMessage  <- c.get[Option[RawMessage]]("referenced_message")
      messageInteraction <- c.get[Option[MessageInteraction]]("interaction")
      components         <- c.get[Option[Seq[ActionRow]]]("components")
      thread             <- c.get[Option[RawChannel]]("thread")
    } yield RawMessage(
      id,
      channelId,
      guildId,
      author,
      member,
      content,
      timestamp,
      editedTimestamp,
      tts,
      mentionEveryone,
      mentions,
      mentionRoles,
      mentionChannels,
      attachment,
      embeds,
      reactions,
      nonce,
      pinned,
      tpe,
      activity,
      application,
      applicationId,
      messageReference,
      flags,
      stickers,
      stickerItems,
      referencedMessage,
      messageInteraction,
      components,
      thread
    )
  }

  implicit val voiceStateCodec: Codec[VoiceState] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val inviteGuildCodec: Codec[InviteGuild] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val inviteChannelCodec: Codec[InviteChannel] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val inviteStageInstanceMemberCodec: Codec[InviteStageInstanceMember] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val inviteStageInstanceCodec: Codec[InviteStageInstance] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val inviteCodec: Codec[Invite] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val inviteWithMetadataCodec: Codec[InviteWithMetadata] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val guildWidgetSettingsCodec: Codec[GuildWidgetSettings] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val integrationAccountCodec: Codec[IntegrationAccount] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val partialIntegrationCodec: Codec[PartialIntegration] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val integrationApplicationCodec: Codec[IntegrationApplication] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val discordIntegrationCodec: Codec[DiscordIntegration] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val externalIntegrationCodec: Codec[ExternalIntegration] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val integrationCodec: Codec[Integration] = Codec.from(
    (c: HCursor) =>
      for {
        tpe <- c.get[IntegrationType]("type")
        res <- tpe match {
          case IntegrationType.Discord => c.as[DiscordIntegration]
          case _                       => c.as[ExternalIntegration]
        }
      } yield res,
    {
      case a: DiscordIntegration  => a.asJson
      case a: ExternalIntegration => a.asJson
    }
  )

  implicit val voiceRegionCodec: Codec[VoiceRegion] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val rawEmojiCodec: Codec[RawEmoji] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val connectionCodec: Codec[Connection] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val webhookSourceGuildDecoder: Codec[WebhookSourceGuild] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val webhookSourceChannelDecoder: Codec[WebhookSourceChannel] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val webhookCodec: Codec[Webhook] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val auditLogDecoder: Decoder[AuditLog] =
    derivation.deriveDecoder(derivation.renaming.snakeCase, false, None)

  implicit val auditLogEntryDecoder: Decoder[AuditLogEntry] =
    derivation.deriveDecoder(derivation.renaming.snakeCase, false, None)

  implicit val optionalAuditLogInfoDecoder: Decoder[OptionalAuditLogInfo] =
    derivation.deriveDecoder(derivation.renaming.snakeCase, false, None)

  implicit val partialRoleCodec: Codec[PartialRole] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val templateCodec: Codec[GuildTemplate] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val guildWidgetCodec: Codec[GuildWidget] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val guildWidgetChannelCodec: Codec[GuildWidgetChannel] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val guildWidgetMemberCodec: Codec[GuildWidgetMember] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val auditLogChangeDecoder: Decoder[AuditLogChange[_]] = (c: HCursor) => {

    def mkChange[A: Decoder, B](create: (A, A) => B): Either[DecodingFailure, B] =
      for {
        oldVal <- c.get[A]("old_value")
        newVal <- c.get[A]("new_value")
      } yield create(oldVal, newVal)

    c.get[String]("key").flatMap {
      case "name"                          => mkChange(AuditLogChange.Name)
      case "description"                   => mkChange(AuditLogChange.Description)
      case "icon_hash"                     => mkChange(AuditLogChange.IconHash)
      case "splash_hash"                   => mkChange(AuditLogChange.SplashHash)
      case "owner_id"                      => mkChange(AuditLogChange.OwnerId)
      case "region"                        => mkChange(AuditLogChange.Region)
      case "preferred_locale"              => mkChange(AuditLogChange.PreferredLocale)
      case "afk_channel_id"                => mkChange(AuditLogChange.AfkChannelId)
      case "afk_timeout"                   => mkChange(AuditLogChange.AfkTimeout)
      case "rules_channel_id"              => mkChange(AuditLogChange.RulesChannelId)
      case "public_updates_channel_id"     => mkChange(AuditLogChange.PublicUpdatesChannelId)
      case "mfa_level"                     => mkChange(AuditLogChange.MfaLevel)
      case "verification_level"            => mkChange(AuditLogChange.VerificationLevel)
      case "explicit_content_filter"       => mkChange(AuditLogChange.ExplicitContentFilter)
      case "default_message_notifications" => mkChange(AuditLogChange.DefaultMessageNotification)
      case "vanity_url_code"               => mkChange(AuditLogChange.VanityUrlCode)
      case "$add"                          => mkChange(AuditLogChange.$Add)
      case "$remove"                       => mkChange(AuditLogChange.$Remove)
      case "prune_delete_days"             => mkChange(AuditLogChange.PruneDeleteDays)
      case "widget_enabled"                => mkChange(AuditLogChange.WidgetEnabled)
      case "widget_channel_id"             => mkChange(AuditLogChange.WidgetChannelId)
      case "system_channel_id"             => mkChange(AuditLogChange.SystemChannelId)
      case "position"                      => mkChange(AuditLogChange.Position)
      case "topic"                         => mkChange(AuditLogChange.Topic)
      case "bitrate"                       => mkChange(AuditLogChange.Bitrate)
      case "permission_overwrites"         => mkChange(AuditLogChange.PermissionOverwrites)
      case "nsfw"                          => mkChange(AuditLogChange.NSFW)
      case "application_id"                => mkChange(AuditLogChange.ApplicationId)
      case "rate_limit_per_user"           => mkChange(AuditLogChange.RateLimitPerUser)
      case "permissions"                   => mkChange(AuditLogChange.Permissions)
      case "color"                         => mkChange(AuditLogChange.Color)
      case "hoist"                         => mkChange(AuditLogChange.Hoist)
      case "mentionable"                   => mkChange(AuditLogChange.Mentionable)
      case "allow"                         => mkChange(AuditLogChange.Allow)
      case "deny"                          => mkChange(AuditLogChange.Deny)
      case "code"                          => mkChange(AuditLogChange.Code)
      case "channel_id"                    => mkChange(AuditLogChange.InviteChannelId)
      case "inviter_id"                    => mkChange(AuditLogChange.InviterId)
      case "max_uses"                      => mkChange(AuditLogChange.MaxUses)
      case "uses"                          => mkChange(AuditLogChange.Uses)
      case "max_age"                       => mkChange(AuditLogChange.MaxAge)
      case "temporary"                     => mkChange(AuditLogChange.Temporary)
      case "deaf"                          => mkChange(AuditLogChange.Deaf)
      case "mute"                          => mkChange(AuditLogChange.Mute)
      case "nick"                          => mkChange(AuditLogChange.Nick)
      case "avatar_hash"                   => mkChange(AuditLogChange.AvatarHash)
      case "id"                            => mkChange(AuditLogChange.Id)
      case "type"             => mkChange(AuditLogChange.TypeInt).left.flatMap(_ => mkChange(AuditLogChange.TypeString))
      case "enable_emoticons" => mkChange(AuditLogChange.EnableEmoticons)
      case "expire_behavior"  => mkChange(AuditLogChange.ExpireBehavior)
      case "expire_grace_period" => mkChange(AuditLogChange.ExpireGracePeriod)
      case "user_limit"          => mkChange(AuditLogChange.UserLimit)
      case "privacy_level"       => mkChange(AuditLogChange.PrivacyLevel)
    }
  }

  implicit val rawBanCodec: Codec[RawBan] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val clientStatusCodec: Codec[ClientStatus] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val teamCodec: Codec[Team] = derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val teamMemberCodec: Codec[TeamMember] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val allowedMentionCodec: Codec[AllowedMention] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val applicationCommandCodec: Codec[ApplicationCommand] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val applicationCommandOptionCodec: Codec[ApplicationCommandOption] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val interactionRawGuildMemberCodec: Codec[InteractionRawGuildMember] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val interactionChannelCodec: Codec[InteractionChannel] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val applicationCommandInteractionDataResolvedCodec: Codec[ApplicationCommandInteractionDataResolved] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val applicationCommandInteractionDataCodec: Codec[ApplicationCommandInteractionData] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val applicationComponentInteractionDataCodec: Codec[ApplicationComponentInteractionData] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val applicationInteractionDataCodec: Codec[ApplicationInteractionData] =
    Codec.from(
      (c: HCursor) =>
        c.as[ApplicationComponentInteractionData]
          .orElse(c.as[ApplicationCommandInteractionData])
          .orElse(c.as[Json].map(ApplicationUnknownInteractionData)),
      {
        case a: ApplicationCommandInteractionData    => a.asJson
        case a: ApplicationComponentInteractionData  => a.asJson
        case ApplicationUnknownInteractionData(data) => data
      }
    )

  implicit val interactionResponseCodec: Codec[RawInteractionResponse] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val interactionApplicationCommandCallbackDataCodec: Codec[RawInteractionApplicationCommandCallbackData] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val applicationCommandOptionChoiceCodec: Codec[ApplicationCommandOptionChoice] = Codec.from(
    (c: HCursor) =>
      for {
        name  <- c.get[String]("name")
        value <- c.get[String]("value").map(Left(_)).orElse(c.get[Int]("value").map(Right(_)))
      } yield ApplicationCommandOptionChoice(name, value),
    (a: ApplicationCommandOptionChoice) => Json.obj("name" := a.name, "value" := a.value.fold(_.asJson, _.asJson))
  )

  implicit val applicationCommandInteractionDataOptionCodec: Codec[ApplicationCommandInteractionDataOption[_]] = {
    Codec.from(
      (c: HCursor) => {

        c.get[ApplicationCommandOptionType]("type")
          .flatMap[DecodingFailure, ApplicationCommandInteractionDataOption[_]] { tpe =>
            for {
              name     <- c.get[String]("name")
              rawValue <- c.get[Option[Json]](tpe.valueJsonName)
              value    <- rawValue.traverse(tpe.decodeJson)
            } yield ApplicationCommandInteractionDataOption[tpe.Res](name, tpe, value)
          }

        /*
        for {
          name     <- c.get[String]("name")
          tpe      <- c.get[ApplicationCommandOptionType]("type")
          rawValue <- c.get[Option[Json]](tpe.valueJsonName)
          value    <- rawValue.traverse(tpe.decodeJson)
        } yield ApplicationCommandInteractionDataOption[tpe.Res](name, tpe, value)
         */
      },
      { case ApplicationCommandInteractionDataOption(name, tpe, value) =>
        Json.obj(
          "name"            := name,
          "type"            := (tpe: ApplicationCommandOptionType),
          tpe.valueJsonName := value.map(tpe.encodeJson)
        )
      }
    )
  }

  implicit val interactionCodec: Codec[RawInteraction] = Codec.from(
    (c: HCursor) =>
      for {
        id            <- c.get[InteractionId]("id")
        applicationId <- c.get[RawSnowflake]("application_id")
        tpe           <- c.get[InteractionType]("type")
        data          <- c.get[Option[ApplicationInteractionData]]("data")
        guildId       <- c.get[Option[GuildId]]("guild_id")
        channelId     <- c.get[TextChannelId]("channel_id")
        member        <- c.get[Option[RawGuildMember]]("member")
        permissions   <- c.downField("member").get[Option[Permission]]("permissions")
        user          <- c.get[Option[User]]("user")
        token         <- c.get[String]("token")
        message       <- c.get[Option[RawMessage]]("message")
        version       <- c.get[Option[Int]]("version")
      } yield RawInteraction(
        id,
        applicationId,
        tpe,
        data,
        guildId,
        channelId,
        member,
        permissions,
        user,
        token,
        message,
        version
      ),
    (a: RawInteraction) =>
      Json.obj(
        "id"             := a.id,
        "application_id" := a.applicationId,
        "type"           := a.tpe,
        "data"           := a.data,
        "guild_id"       := a.guildId,
        "channel_id"     := a.channelId,
        "member" := a.member.map(
          _.asJson.withObject(o => Json.fromJsonObject(o.add("permissions", a.memberPermission.get.asJson)))
        ),
        "user"    := a.user,
        "token"   := a.token,
        "message" := a.message,
        "version" := a.version
      )
  )

  implicit val applicationCommandPermissionsCodec: Codec[ApplicationCommandPermissions] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)

  implicit val guildApplicationCommandPermissionsCodec: Codec[GuildApplicationCommandPermissions] =
    derivation.deriveCodec(derivation.renaming.snakeCase, false, None)
}
object DiscordProtocol extends DiscordProtocol
