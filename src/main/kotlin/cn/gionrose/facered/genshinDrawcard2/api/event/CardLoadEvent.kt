package cn.gionrose.facered.genshinDrawcard2.api.event

import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import taboolib.platform.type.BukkitProxyEvent

/**
 * @description 卡片加载事件
 * @author facered
 * @date 2023/10/9 13:40
 */
class CardLoadEvent (val allCards: ArrayList<Card>): BukkitProxyEvent()