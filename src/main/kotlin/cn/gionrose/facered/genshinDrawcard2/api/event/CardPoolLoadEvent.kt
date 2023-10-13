package cn.gionrose.facered.genshinDrawcard2.api.event

import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardPool
import taboolib.platform.type.BukkitProxyEvent

/**
 * @description 奖池加载事件
 * @author facered
 * @date 2023/10/9 13:42
 */
class CardPoolLoadEvent (val loadedCards :HashMap<String, Card>, val allCardPools: ArrayList<CardPool>): BukkitProxyEvent()