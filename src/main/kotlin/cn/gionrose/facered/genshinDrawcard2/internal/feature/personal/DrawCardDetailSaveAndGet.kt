package cn.gionrose.facered.genshinDrawcard2.internal.feature.personal

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.CardDrawDetail
import cn.gionrose.facered.genshinDrawcard2.internal.feature.database.GenshinDrawCard2Database
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

/**
 * @description 抽卡信息存储和获取
 * @author facered
 * @date 2023/10/13 21:22
 */
private object DrawCardDetailSaveAndGet {

    @SubscribeEvent(EventPriority.LOWEST)
    fun quit(event: PlayerQuitEvent) {
        val player = event.player
        GenshinDrawcard2.cardDrawDetailManager.unregisterDetail(player.uniqueId)
    }

    @SubscribeEvent(EventPriority.LOWEST)
    fun join(event: PlayerJoinEvent) {
        val player = event.player
        var details: MutableList<CardDrawDetail> = mutableListOf()
            GenshinDrawcard2.cardPoolManager.values.map { it.key }.forEach {
                details = GenshinDrawcard2.cardDrawDetailManager[player.uniqueId]!!
                details.add(CardDrawDetail(it))
            }
        GenshinDrawcard2.cardPoolManager.values.map { it.key }.forEach { poolName ->
            val cardDrawDetail = GenshinDrawCard2Database.selectPlayerDrawCount(player, poolName)
            cardDrawDetail?.let {databaseDetail ->
                details.forEach {detail ->
                    if (databaseDetail.key == detail.key)
                    {
                        detail.coverOf(databaseDetail)
                    }
                }
            }
        }
    }

}