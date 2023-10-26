package cn.gionrose.facered.genshinDrawcard2.internal.manager

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardPool
import cn.gionrose.facered.genshinDrawcard2.api.card.CardStarGrade
import cn.gionrose.facered.genshinDrawcard2.api.card.StarGradeContainer
import cn.gionrose.facered.genshinDrawcard2.api.event.CardPoolLoadEvent
import cn.gionrose.facered.genshinDrawcard2.api.manager.CardPoolManager
import cn.gionrose.facered.genshinDrawcard2.internal.manager.GenshinDrawcard2ConfigManagerImpl.debug
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * @description 奖池管理器的实现
 * @author facered
 * @date 2023/10/7 19:53
 */
object CardPoolManagerImpl: CardPoolManager () {

    override val key = "CardPoolManager"

    override val priority = 3

    override val subPouvoir = GenshinDrawcard2

    override fun registerPool(cardPool: CardPool) {
        register(cardPool)
    }

    override fun unregisterPool(cardPool: CardPool) {
        remove(cardPool.key)
    }

    override fun releaseAllPools() {
        clear()
    }

    override fun registerCard(poolName: String, card: Card, weight: Int) {
        get(poolName)?.registerCard( card, weight)
    }

    override fun registerCard(poolName: String, card: List<Card>, weight: Int) {
        get(poolName)?.registerCard(card, weight)
    }

    override fun registerCard(pool: CardPool, card: Card, weight: Int): CardPool {
        pool.registerCard(card, weight)
        return pool
    }

    override fun registerCard(pool: CardPool, card: List<Card>, weight: Int): CardPool {
        pool.registerCard(card, weight)
        return pool
    }

    override fun random(pool: CardPool, grade: CardStarGrade, isUpRandom: Boolean): Card? {
        GenshinDrawcard2.starGradeContainerManager.getContainer(pool).values().forEach {
            if (it.element.grade == grade)
                return if (isUpRandom) GenshinDrawcard2.starGradeContainerManager.upRandomCard(it.element)  else GenshinDrawcard2.starGradeContainerManager.randomCard (it.element)
        }
        return null
    }

    override fun randomStarGradeContainer(pool: CardPool): StarGradeContainer? {
        return GenshinDrawcard2.starGradeContainerManager.randomStarGradeContainer(pool)
    }

    override fun createCardPool(
        key: String,
        threeStarWeight: Int,
        fourStarWeight: Int,
        fiveStarWeight: Int,
        threeSmallGuaranteeCount: Int,
        fourSmallGuaranteeCount: Int,
        fiveSmallGuaranteeCount: Int,
        isEnabledThreeStarBigGuarantee: Boolean,
        isEnabledFourStarBigGuarantee: Boolean,
        isEnabledFiveStarBigGuarantee: Boolean,
        period: Long
    ): CardPool {
        return CardPool.create(key, threeStarWeight, fourStarWeight, fiveStarWeight, threeSmallGuaranteeCount, fourSmallGuaranteeCount, fiveSmallGuaranteeCount, isEnabledThreeStarBigGuarantee, isEnabledFourStarBigGuarantee, isEnabledFiveStarBigGuarantee, period)
    }


    //------------------------------------------------------------------------------------
    //                          抽卡管理器的调用周期
    //------------------------------------------------------------------------------------



    override fun onActive() {

        registerPoolByListener ()

    }

    private fun registerPoolByListener ()
    {
        val cards = mutableMapOf<String, Card>()
        GenshinDrawcard2.cardManager.getAllCards().forEach {
            cards[it.key] = it
        }

        releaseAllPools()
        val cardPoolLoadEvent = CardPoolLoadEvent (HashMap(cards), ArrayList ())
        cardPoolLoadEvent.call()


        if (cardPoolLoadEvent.isCancelled)
            return

        cardPoolLoadEvent.allCardPools.forEach (::registerPool)

    }

    override fun onReload() {
        onActive()
    }




    //------------------------------------------------------------------------------------
    //                          CardPool 拓展函数
    //------------------------------------------------------------------------------------



    private fun CardPool.registerCard (card: Card, weight: Int)
    {
        val containers = GenshinDrawcard2.starGradeContainerManager.getContainer(this).values()
        for (container in containers) {

                if (card.grade == container.element.grade)
                {
                    GenshinDrawcard2.starGradeContainerManager.registerCard(container.element, card, weight)

                    debug {
                        console().sendLang("奖池_添加_卡片", card.key, container.element.grade.content)
                    }
                }

        }
    }

    private fun CardPool.registerCard ( card: List<Card>, weight: Int)
    {
        card.forEach {
            registerCard( it, weight )
        }
    }

    //------------------------------------------------------------------------------------
    //                          重写父类
    //------------------------------------------------------------------------------------

    override fun register(value: CardPool) {

        if (containsKey(value.key)) return
        super.register(value)
        debug {
            console().sendLang("奖池_添加", value.key )
        }

    }

    override fun remove(key: String): CardPool? {
        val result = super.remove(key)
        result?.let{ debug {
            console().sendLang("奖池_移除", key)
        }}
        return result
    }

    override fun clear() {
        this.keys.forEach (::remove)
    }


}