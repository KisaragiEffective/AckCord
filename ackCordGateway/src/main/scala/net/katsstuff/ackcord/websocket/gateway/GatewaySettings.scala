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
package net.katsstuff.ackcord.websocket.gateway

import java.time.Instant

import net.katsstuff.ackcord.data.PresenceStatus
import net.katsstuff.ackcord.data.raw.RawActivity

/**
  * All the settings used by AckCord when connecting and similar
  *
  * @param token The token for the bot
  * @param largeThreshold The large threshold
  * @param shardNum The shard index of this
  * @param shardTotal The amount of shards
  * @param idleSince If the bot has been idle, set the time since
  * @param activity Send an activity when connecting
  * @param status The status to use when connecting
  * @param afk If the bot should be afk when connecting
  */
case class GatewaySettings(
    token: String,
    largeThreshold: Int = 100,
    shardNum: Int = 0,
    shardTotal: Int = 1,
    idleSince: Option[Instant] = None,
    activity: Option[RawActivity] = None,
    status: PresenceStatus = PresenceStatus.Online,
    afk: Boolean = false
) {
  activity.foreach(_.requireCanSend())
}
