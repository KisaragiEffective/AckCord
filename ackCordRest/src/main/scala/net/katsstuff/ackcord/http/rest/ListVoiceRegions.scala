/*
 * This file is part of AckCord, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 Katrix
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
package net.katsstuff.ackcord.http.rest

import akka.NotUsed
import io.circe._
import net.katsstuff.ackcord.data._
import net.katsstuff.ackcord.http.Routes
import net.katsstuff.ackcord.http.requests.RequestRoute

import net.katsstuff.ackcord.data.DiscordProtocol._

//Place for future audit log requests if they should ever appear

/**
  * List all the voice regions that can be used when creating a guild.
  */
case class ListVoiceRegions[Ctx](context: Ctx = NotUsed: NotUsed)
    extends NoParamsNiceResponseRequest[Seq[VoiceRegion], Ctx] {
  override def route: RequestRoute = Routes.listVoiceRegions

  override def responseDecoder: Decoder[Seq[VoiceRegion]] = Decoder[Seq[VoiceRegion]]
}
