package cn.gionrose.facered.genshinDrawcard2.internal.feature.personal

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.CardDrawDetail
import cn.gionrose.facered.genshinDrawcard2.internal.feature.database.MysqlWrapper
import cn.gionrose.facered.genshinDrawcard2.internal.manager.GenshinDrawcard2ConfigManagerImpl.debug
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

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
        debug{
            console().sendLang("玩家抽卡详情_移除", player.name)
        }
    }

    @SubscribeEvent(EventPriority.LOWEST)
    fun join(event: PlayerJoinEvent) {
        val player = event.player
        var details: MutableList<CardDrawDetail> = mutableListOf()
            GenshinDrawcard2.cardPoolManager.values.map { it.key }.forEach {
                details = GenshinDrawcard2.cardDrawDetailManager[player.uniqueId]!!
                details.add(CardDrawDetail(it))
                debug{
                    console().sendLang("玩家抽卡详情_添加", player.name, it)
                }
            }
        GenshinDrawcard2.cardPoolManager.values.map { it.key }.forEach { poolName ->
            val cardDrawDetail = MysqlWrapper.selectPlayerDrawCount(player, poolName)
            cardDrawDetail?.let {databaseDetail ->
                details.forEach {detail ->
                    if (databaseDetail.key == detail.key)
                    {
                        detail.coverOf(databaseDetail)
                        debug{
                            console().sendLang("玩家抽卡详情_恢复", player.name, poolName)
                        }
                    }
                }
            }
            if (cardDrawDetail == null)
                MysqlWrapper.initPlayerDrawCount(player, poolName)
        }
    }

}