package cn.gionrose.facered.genshinDrawcard2.api.event

import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import taboolib.platform.type.BukkitProxyEvent

/**
 * @description 记录卡片事件
 * @author facered
 * @date 2023/10/25 14:14
 */
class RecordCardEvent (val recordCard: Card): BukkitProxyEvent()