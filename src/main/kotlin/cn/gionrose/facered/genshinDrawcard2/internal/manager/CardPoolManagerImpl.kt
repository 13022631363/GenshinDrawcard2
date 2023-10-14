package cn.gionrose.facered.genshinDrawcard2.internal.manager

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardPool
import cn.gionrose.facered.genshinDrawcard2.api.card.CardStarGrade
import cn.gionrose.facered.genshinDrawcard2.api.card.CardStarGrade.*
import cn.gionrose.facered.genshinDrawcard2.api.event.CardPoolLoadEvent
import cn.gionrose.facered.genshinDrawcard2.api.manager.CardPoolManager
import cn.gionrose.facered.genshinDrawcard2.internal.manager.GenshinDrawcard2ConfigManagerImpl.debug
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.util.*

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

    override fun draw(poolName: String, uuid: UUID): Card? {
        val threeSmallGuarantee = GenshinDrawcard2.cardDrawDetailManager.getCount(uuid, "三星小保底次数")
        val fourSmallGuarantee = GenshinDrawcard2.cardDrawDetailManager.getCount(uuid, "四星小保底次数")
        val fiveSmallGuarantee = GenshinDrawcard2.cardDrawDetailManager.getCount(uuid, "五星小保底次数")
        val threeBigGuarantee = GenshinDrawcard2.cardDrawDetailManager.isTriggerBigGuarantee(uuid, "三星大保底触发")
        val fourBigGuarantee = GenshinDrawcard2.cardDrawDetailManager.isTriggerBigGuarantee(uuid, "四星大保底触发")
        val fiveBigGuarantee = GenshinDrawcard2.cardDrawDetailManager.isTriggerBigGuarantee(uuid, "五星大保底触发")

        val pool = this[poolName] ?: return null
        val result = smallGuarantee(
            pool,
            uuid,
            threeSmallGuarantee,
            fourSmallGuarantee,
            fiveSmallGuarantee,
            threeBigGuarantee,
            fourBigGuarantee,
            fiveBigGuarantee
        )

        afterGuarantee(uuid, result)
        return result

    }

    /**
     * 保底机制后
     */
    private fun afterGuarantee (uuid: UUID,card: Card?)
    {
        card?.let {
            val countDetailName: String
            val bigGuaranteeDetailName: String
            val smallGuaranteeDetailName: String
            when (card.grade)
            {
                FIVE_STAR -> {
                    countDetailName = "五星抽卡总次数"
                    smallGuaranteeDetailName = "五星小保底次数"
                    bigGuaranteeDetailName = "五星大保底触发"
                }

                THREE_STAR -> {
                    countDetailName = "三星抽卡总次数"
                    smallGuaranteeDetailName = "三星小保底次数"
                    bigGuaranteeDetailName = "三星大保底触发"
                }
                FOUR_STAR -> {
                    countDetailName = "四星抽卡总次数"
                    smallGuaranteeDetailName = "四星小保底次数"
                    bigGuaranteeDetailName = "四星大保底触发"
                }
            }
            GenshinDrawcard2.cardDrawDetailManager.setTriggerBigGuarantee(uuid, bigGuaranteeDetailName,!it.isUp)
            GenshinDrawcard2.cardDrawDetailManager.addCount(uuid, smallGuaranteeDetailName,1)
            GenshinDrawcard2.cardDrawDetailManager.addCount(uuid, countDetailName,1)
            GenshinDrawcard2.cardDrawDetailManager.addCount(uuid, "抽卡总次数", 1)
            //todo
            GenshinDrawcard2.cardDrawDetailManager.addCardRecord(uuid, it.clone()){
                val itemMeta = source.itemMeta!!
                itemMeta.lore = listOf("${System.currentTimeMillis()}")
                source.itemMeta = itemMeta
            }
        }
    }

    /**
     * 大保底机制
     */
    private fun bigGuarantee(pool: CardPool, grade: CardStarGrade, isTriggerBigGuarantee: Boolean): Card? {
        return if (when (grade)
        {
            THREE_STAR -> pool.isEnabledThreeStarBigGuarantee
            FOUR_STAR -> pool.isEnabledFourStarBigGuarantee
            FIVE_STAR -> pool.isEnabledFiveStarBigGuarantee
        }) pool.random(isTriggerBigGuarantee, grade)
        else pool.random(false, grade)

    }

    /**
     * 小保底机制
     */
    private fun smallGuarantee(pool: CardPool, uuid: UUID, threeSmallGuarantee: Int, fourSmallGuarantee: Int, fiveSmallGuarantee: Int, threeBigGuarantee: Boolean, fourBigGuarantee: Boolean, fiveBigGuarantee: Boolean): Card? {
        if (fiveSmallGuarantee +1 == pool.fiveSmallGuaranteeCount)
        {
            GenshinDrawcard2.cardDrawDetailManager.clearCount(uuid, "五星小保底次数")
            return bigGuarantee(pool, FIVE_STAR, threeBigGuarantee)
        }
        if (fourSmallGuarantee +1 == pool.fourSmallGuaranteeCount)
        {
            GenshinDrawcard2.cardDrawDetailManager.clearCount(uuid, "四星小保底次数")
            return  bigGuarantee(pool, FOUR_STAR, fourBigGuarantee)
        }

        if (threeSmallGuarantee +1 == pool.threeSmallGuaranteeCount)
        {
            GenshinDrawcard2.cardDrawDetailManager.clearCount(uuid, "三星小保底次数")
            return  bigGuarantee(pool, THREE_STAR, fiveBigGuarantee)
        }


        val container = GenshinDrawcard2.starGradeContainerManager.randomStarGradeContainer(pool)

        container?.also {
            return bigGuarantee(pool, container.grade, when (container.grade)
            {
                THREE_STAR -> threeBigGuarantee
                FOUR_STAR -> fourBigGuarantee
                FIVE_STAR -> fiveBigGuarantee
            })
        } ?: console().sendLang("奖池_星级池_未配置", pool)

       return null
    }




    //------------------------------------------------------------------------------------
    //                          抽卡管理器的调用周期
    //------------------------------------------------------------------------------------



    override fun onEnable() {

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
        onEnable()
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

    private fun CardPool.random (isUpRandom: Boolean = false, grade: CardStarGrade): Card?
    {
        GenshinDrawcard2.starGradeContainerManager.getContainer(this).values().forEach {
            if (it.element.grade == grade)
            {
                debug {
                    if (it.element.size() == 0 )
                        console().sendLang("星级卡片池_长度是0", this.key, it.element.grade.content)
                }
                return if (isUpRandom) GenshinDrawcard2.starGradeContainerManager.upRandomCard(it.element)  else GenshinDrawcard2.starGradeContainerManager.randomCard (it.element)
            }
        }
        return null
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