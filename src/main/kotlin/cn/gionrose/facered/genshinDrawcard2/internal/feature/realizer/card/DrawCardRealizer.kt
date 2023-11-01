package cn.gionrose.facered.genshinDrawcard2.internal.feature.realizer.card

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardPool
import cn.gionrose.facered.genshinDrawcard2.api.card.CardStarGrade
import cn.gionrose.facered.genshinDrawcard2.api.event.RecordCardEvent
import cn.gionrose.facered.genshinDrawcard2.internal.feature.database.GenshinDrawCard2Database
import com.skillw.pouvoir.api.feature.realizer.BaseRealizer
import com.skillw.pouvoir.api.feature.realizer.BaseRealizerManager
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.util.player
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.common.util.unsafeLazy
import taboolib.module.lang.sendLang
import java.text.SimpleDateFormat
import java.util.*

/**
 * @description 抽卡实现器
 * @author facered
 * @date 2023/10/14 21:54
 */
@AutoRegister
internal object DrawCardRealizer: BaseRealizer ("抽卡实现器") {

    override val file by lazy {
        GenshinDrawcard2.config.file!!
    }
    override val manager: BaseRealizerManager by unsafeLazy {
        GenshinDrawcard2.realizerManager
    }

     fun draw(poolName: String, uuid: UUID): Card? {
        val threeSmallGuarantee = GenshinDrawcard2.cardDrawDetailManager.getCount(uuid, poolName,"三星小保底次数")
        val fourSmallGuarantee = GenshinDrawcard2.cardDrawDetailManager.getCount(uuid, poolName,"四星小保底次数")
        val fiveSmallGuarantee = GenshinDrawcard2.cardDrawDetailManager.getCount(uuid, poolName,"五星小保底次数")
        val threeBigGuarantee = GenshinDrawcard2.cardDrawDetailManager.isTriggerBigGuarantee(uuid, poolName,"三星大保底触发")
        val fourBigGuarantee = GenshinDrawcard2.cardDrawDetailManager.isTriggerBigGuarantee(uuid, poolName,"四星大保底触发")
        val fiveBigGuarantee = GenshinDrawcard2.cardDrawDetailManager.isTriggerBigGuarantee(uuid, poolName,"五星大保底触发")

        val pool = GenshinDrawcard2.cardPoolManager[poolName] ?: return null
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

        afterGuarantee(uuid, result, poolName)
        return result

    }

    /**
     * 保底机制后
     */
    private fun afterGuarantee (uuid: UUID, card: Card?, poolName: String)
    {
        card?.let {
            val countDetailName: String
            val bigGuaranteeDetailName: String
            val smallGuaranteeDetailName: String
            when (card.grade)
            {
                CardStarGrade.FIVE_STAR -> {
                    countDetailName = "五星抽卡总次数"
                    smallGuaranteeDetailName = "五星小保底次数"
                    bigGuaranteeDetailName = "五星大保底触发"
                }

                CardStarGrade.THREE_STAR -> {
                    countDetailName = "三星抽卡总次数"
                    smallGuaranteeDetailName = "三星小保底次数"
                    bigGuaranteeDetailName = "三星大保底触发"
                }
                CardStarGrade.FOUR_STAR -> {
                    countDetailName = "四星抽卡总次数"
                    smallGuaranteeDetailName = "四星小保底次数"
                    bigGuaranteeDetailName = "四星大保底触发"
                }
            }
            GenshinDrawcard2.cardDrawDetailManager.setTriggerBigGuarantee(uuid, poolName, bigGuaranteeDetailName,!it.isUp)
            GenshinDrawcard2.cardDrawDetailManager.addCount(uuid, poolName, smallGuaranteeDetailName,1)
            GenshinDrawcard2.cardDrawDetailManager.addCount(uuid, poolName, countDetailName,1)
            GenshinDrawcard2.cardDrawDetailManager.addCount(uuid, poolName, "抽卡总次数", 1)

            val recordCardEvent = RecordCardEvent (it.clone ())
            recordCardEvent.call()

            submit (async = true){

                    GenshinDrawcard2.cardDrawDetailManager[uuid]!!.forEach { detail ->
                        if (detail.key == poolName)
                        {
                            GenshinDrawCard2Database.updatePlayerDrawCount(uuid.player()!!, detail)
                        }
                    }

                GenshinDrawCard2Database.insertRecord(uuid.player()!!,recordCardEvent.recordCard,  SimpleDateFormat ("yyyy-MM-dd HH:mm:ss").format(Date()).toString())
            }
        }
    }

    /**
     * 大保底机制
     */
    private fun bigGuarantee(pool: CardPool, grade: CardStarGrade, isTriggerBigGuarantee: Boolean): Card? {
        return if (when (grade)
            {
                CardStarGrade.THREE_STAR -> pool.isEnabledThreeStarBigGuarantee
                CardStarGrade.FOUR_STAR -> pool.isEnabledFourStarBigGuarantee
                CardStarGrade.FIVE_STAR -> pool.isEnabledFiveStarBigGuarantee
            }) GenshinDrawcard2.cardPoolManager.random(pool, grade, isTriggerBigGuarantee)
                else GenshinDrawcard2.cardPoolManager.random(pool, grade, false)
    }

    /**
     * 小保底机制
     */
    private fun smallGuarantee(pool: CardPool, uuid: UUID, threeSmallGuarantee: Int, fourSmallGuarantee: Int, fiveSmallGuarantee: Int, threeBigGuarantee: Boolean, fourBigGuarantee: Boolean, fiveBigGuarantee: Boolean): Card? {
        if (fiveSmallGuarantee +1 == pool.fiveSmallGuaranteeCount)
        {
            GenshinDrawcard2.cardDrawDetailManager.clearCount(uuid, pool.key,  "五星小保底次数")
            return bigGuarantee(pool, CardStarGrade.FIVE_STAR, threeBigGuarantee)
        }
        if (fourSmallGuarantee +1 == pool.fourSmallGuaranteeCount)
        {
            GenshinDrawcard2.cardDrawDetailManager.clearCount(uuid, pool.key,  "四星小保底次数")
            return  bigGuarantee(pool, CardStarGrade.FOUR_STAR, fourBigGuarantee)
        }

        if (threeSmallGuarantee +1 == pool.threeSmallGuaranteeCount)
        {
            GenshinDrawcard2.cardDrawDetailManager.clearCount(uuid, pool.key,  "三星小保底次数")
            return  bigGuarantee(pool, CardStarGrade.THREE_STAR, fiveBigGuarantee)
        }


        val container = GenshinDrawcard2.cardPoolManager.randomStarGradeContainer(pool)

        container?.also {
            return bigGuarantee(pool, container.grade, when (container.grade)
            {
                CardStarGrade.THREE_STAR -> threeBigGuarantee
                CardStarGrade.FOUR_STAR -> fourBigGuarantee
                CardStarGrade.FIVE_STAR -> fiveBigGuarantee
            })
        } ?: console().sendLang("奖池_星级池_未配置", pool)

        return null
    }
}