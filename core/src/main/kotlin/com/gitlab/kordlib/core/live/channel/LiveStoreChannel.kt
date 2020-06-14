package com.gitlab.kordlib.core.live.channel

import com.gitlab.kordlib.common.annotation.KordPreview
import com.gitlab.kordlib.core.entity.Entity
import com.gitlab.kordlib.core.entity.channel.StoreChannel
import com.gitlab.kordlib.core.event.Event
import com.gitlab.kordlib.core.event.channel.StoreChannelCreateEvent
import com.gitlab.kordlib.core.event.channel.StoreChannelDeleteEvent
import com.gitlab.kordlib.core.event.channel.StoreChannelUpdateEvent
import com.gitlab.kordlib.core.event.guild.GuildDeleteEvent

@KordPreview
@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("Message channels are allowed to change type during their lifetime. As such, there's no guarantee that the channel will stay a StoreChannel", level = DeprecationLevel.WARNING)
fun StoreChannel.live() = LiveStoreChannel(this)

@KordPreview
@Deprecated("Message channels are allowed to change type during their lifetime. As such, there's no guarantee that the channel will stay a StoreChannel", level = DeprecationLevel.WARNING)
class LiveStoreChannel(channel: StoreChannel) : LiveChannel(), Entity by channel {

    override var channel: StoreChannel = channel
        private set

    override fun update(event: Event) = when (event) {
        is StoreChannelCreateEvent -> channel = event.channel
        is StoreChannelUpdateEvent -> channel = event.channel
        is StoreChannelDeleteEvent -> shutDown()

        is GuildDeleteEvent -> shutDown()

        else -> Unit
    }

}