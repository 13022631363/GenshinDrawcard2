package cn.gionrose.facered.genshinDrawcard2.internal.feature.compat

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.CardStarGrade
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common5.cint

/**
 * @description 抽卡变量
 * @author facered
 * @date 2023/10/26 22:01
 */
internal object GenshinDrawcardPapi: PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return "dc"
    }

    override fun getAuthor(): String {
        return "facered"
    }

    override fun getVersion(): String {
        return "1.0.0"
    }

    override fun getName(): String {
        return "dc"
    }

    /**
     * dc_poolName_draw_count_3
     * dc_poolName_draw_count_4
     * dc_poolName_draw_count_5
     *
     * dc_poolName_small_guarantee_count_3
     * dc_poolName_small_guarantee_count_4
     * dc_poolName_small_guarantee_count_5
     *
     * dc_poolName_big_guarantee_isEnable_3
     * dc_poolName_big_guarantee_isEnable_4
     * dc_poolName_big_guarantee_isEnable_5
     *
     * dc_poolName_total_count
     */

    @Awake (value = LifeCycle.ENABLE)
    fun init ()
    {
       register()
    }
    override fun onPlaceholderRequest(player: Player?, params: String): String {

        val lowercase = params.lowercase()
        val parameters = lowercase.split("_")
        val poolName = parameters[0]
        val cardDrawDetails = GenshinDrawcard2.cardDrawDetailManager[player!!.uniqueId]!!

        cardDrawDetails.forEach {

            if (it.key == poolName)
            {
                when (parameters.size)
                {
                    3 -> if (parameters[1] == "total" && parameters[2] == "count")
                                return it["抽卡总次数"].toString()
                    4 ->{
                        val gradle = CardStarGrade.getStarGrade(parameters[3].cint)
                        if (parameters[1] == "draw" && parameters[2] == "count")
                        {
                            return when (gradle)
                            {
                                CardStarGrade.THREE_STAR -> it["三星抽卡总次数"].toString()
                                CardStarGrade.FOUR_STAR -> it["四星抽卡总次数"].toString()
                                CardStarGrade.FIVE_STAR -> it["五星抽卡总次数"].toString()
                            }
                        }
                    }

                    5 ->{
                        val guarantee = parameters[1]
                        val gradle = CardStarGrade.getStarGrade(parameters[4].cint)
                        if (guarantee == "small")
                        {
                            if (!(parameters[2] == "guarantee" && parameters[3] == "count"))
                                return@forEach
                            return when (gradle) {
                                CardStarGrade.THREE_STAR -> it["三星小保底次数"].toString()
                                CardStarGrade.FOUR_STAR -> it["四星小保底次数"].toString()
                                CardStarGrade.FIVE_STAR -> it["五星小保底次数"].toString()
                            }
                        }
                        else if (guarantee == "big")
                        {
                            if (!(parameters[2] == "guarantee" && parameters[3] == "isenable"))
                                return@forEach
                            return when (gradle)
                            {
                                CardStarGrade.THREE_STAR -> it["三星大保底触发"].toString()
                                CardStarGrade.FOUR_STAR -> it["四星大保底触发"].toString()
                                CardStarGrade.FIVE_STAR -> it["五星大保底触发"].toString()
                            }
                        }
                    }
                }
            }


        }
        return "${name}_$params"
    }
}