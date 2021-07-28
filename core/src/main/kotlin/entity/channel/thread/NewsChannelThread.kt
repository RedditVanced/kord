package dev.kord.core.entity.channel.thread

import dev.kord.core.Kord
import dev.kord.core.cache.data.ChannelData
import dev.kord.core.entity.channel.NewsChannel
import dev.kord.core.supplier.EntitySupplier
import dev.kord.core.supplier.EntitySupplyStrategy

/**
 * A thread channel instance whose parent is a [NewsChannel].
 */
class NewsChannelThread(
    override val data: ChannelData,
    override val kord: Kord,
    override val supplier: EntitySupplier = kord.defaultSupplier
) : ThreadChannel {


    override suspend fun asChannel(): NewsChannelThread = super.asChannel() as NewsChannelThread

    override suspend fun asChannelOrNull(): NewsChannelThread? = super.asChannelOrNull() as? NewsChannelThread


    override fun withStrategy(strategy: EntitySupplyStrategy<*>): NewsChannelThread {
        return NewsChannelThread(data, kord, strategy.supply(kord))
    }
}