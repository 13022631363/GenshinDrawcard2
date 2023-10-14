package cn.gionrose.facered.genshinDrawcard2.api.manager

import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardPool
import cn.gionrose.facered.genshinDrawcard2.api.card.CardStarGrade
import cn.gionrose.facered.genshinDrawcard2.api.card.StarGradeContainer
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.LowerKeyMap


/**
 * @description 奖池管理器
 * @author facered
 * @date 2023/10/7 13:36
 */
abstract class CardPoolManager: LowerKeyMap<CardPool>(), Manager
{

    /**
     * 注册奖池
     */
    abstract fun registerPool (cardPool: CardPool)

    /**
     * 注销奖池
     */
    abstract fun unregisterPool (cardPool: CardPool)

    /**
     * 释放所有奖池
     */
    abstract fun releaseAllPools ()

    /**
     * 往指定管理器中的某个奖池注册一个卡片
     */
    abstract fun registerCard (poolName: String, card: Card, weight: Int)

    /**
     * 往指定管理器中的某个奖池注册多个卡片 权重相同
     */
    abstract fun registerCard (poolName:String, card: List<Card>, weight: Int)


    /**
     * 往奖池中注册一个卡片
     */
    abstract fun registerCard (pool: CardPool, card: Card, weight: Int): CardPool

    /**
     * 往奖池中注册多个卡片 权重相同
     */
    abstract fun registerCard (pool: CardPool, card: List<Card>, weight: Int): CardPool


    abstract fun random (pool: CardPool, grade: CardStarGrade, isUpRandom: Boolean): Card?

    abstract fun randomStarGradeContainer(pool: CardPool): StarGradeContainer?

}