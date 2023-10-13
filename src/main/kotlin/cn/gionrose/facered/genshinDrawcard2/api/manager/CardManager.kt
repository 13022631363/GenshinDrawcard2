package cn.gionrose.facered.genshinDrawcard2.api.manager

import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.LowerKeyMap


/**
 * @description 卡片管理器
 * @author facered
 * @date 2023/10/6 22:19
 */
abstract class CardManager: LowerKeyMap<Card>(), Manager
{
    /**
     * 注册卡片到管理器
     * @param card 卡片
     */
    abstract fun registerCard (card: List<Card>)

    /**
     * 注册卡片到管理器
     * @param card 卡片
     */
    abstract fun registerCard (card: Card)

    /**
     * 从管理器中注销卡片 (通过卡片名)
     * @param cardKeys 卡片名集合
     */
    abstract fun unregisterCard (cardKeys: List<String>)

    /**
     * 将管理器中的卡片全部清除
     */
    abstract fun releaseAllCards ()

    /**
     * 从管理器中获取所有卡片
     */
    abstract fun getAllCards (): List<Card>

    /**
     * 通过卡片名 获取卡片
     */
    abstract fun getCard (cardName: String): Card?
}

