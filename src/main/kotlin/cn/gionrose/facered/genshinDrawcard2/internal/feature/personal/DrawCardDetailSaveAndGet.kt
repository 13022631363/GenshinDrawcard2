package cn.gionrose.facered.genshinDrawcard2.internal.feature.personal

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
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
        PersonalDrawCardDetailData.pushAttrData(player)
        GenshinDrawcard2.cardDrawDetailManager.unregisterDetail(player.uniqueId)
    }

    @SubscribeEvent(EventPriority.LOWEST)
    fun join(event: PlayerJoinEvent) {
        val player = event.player
        PersonalDrawCardDetailData.pullAttrData(player)?.let {
            GenshinDrawcard2.cardDrawDetailManager[it.key] = it.detail
        }
    }

}