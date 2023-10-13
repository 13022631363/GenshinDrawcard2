package cn.gionrose.facered.genshinDrawcard2.api.event

import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

/**
 * @description 派发卡片事件
 * @author facered
 * @date 2023/10/13 10:15
 */
class HandingOutCardEvent (val player: Player, val drawedCard: List<Card>): BukkitProxyEvent ()
{
    override val allowCancelled = false
}