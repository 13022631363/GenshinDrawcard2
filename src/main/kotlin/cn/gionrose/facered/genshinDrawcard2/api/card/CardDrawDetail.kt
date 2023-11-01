package cn.gionrose.facered.genshinDrawcard2.api.card


import com.skillw.pouvoir.api.plugin.map.LowerMap
import com.skillw.pouvoir.api.plugin.map.component.Keyable
import taboolib.common5.cbool
import taboolib.common5.cint


/**
 * @description 抽卡详情
 * @author facered
 * @date 2023/10/7 23:22
 */
class CardDrawDetail(override val key: String) : LowerMap<Any>(), Keyable<String>{

    init {
        this["三星小保底次数"] = 0
        this["四星小保底次数"] = 0
        this["五星小保底次数"] = 0
        this["三星抽卡总次数"] = 0
        this["四星抽卡总次数"] = 0
        this["五星抽卡总次数"] = 0
        this["三星大保底触发"] = false
        this["四星大保底触发"] = false
        this["五星大保底触发"] = false
        this["抽卡总次数"] = 0
    }

    fun serialize (): Map<String, Any>
    {
        val result = mutableMapOf<String, Any>()
//        result["卡池名"] = this.key
        result["三星小保底次数"] = this["三星小保底次数"]!!
        result["四星小保底次数"] = this["四星小保底次数"]!!
        result["五星小保底次数"] = this["五星小保底次数"]!!
        result["三星抽卡总次数"] = this["三星抽卡总次数"]!!
        result["四星抽卡总次数"] = this["四星抽卡总次数"]!!
        result["五星抽卡总次数"] = this["五星抽卡总次数"]!!
        result["三星大保底触发"] = this["三星大保底触发"]!!
        result["四星大保底触发"] = this["四星大保底触发"]!!
        result["五星大保底触发"] = this["五星大保底触发"]!!
        result["抽卡总次数"] = this["抽卡总次数"]!!

        //todo
        result.forEach { t, u ->
            println ("$t ==> $u")
        }

        return result
    }


    override fun toString(): String {
      return "卡池名 -> $key"+ "三星小保底次数 -> ${get("三星小保底次数")}" +
        "四星小保底次数 -> ${get("四星小保底次数")}" +
        "五星小保底次数 -> ${get("五星小保底次数")}" +
        "三星抽卡总次数 -> ${get("三星抽卡总次数")}" +
        "四星抽卡总次数 -> ${get("四星抽卡总次数")}" +
        "五星抽卡总次数 -> ${get("五星抽卡总次数")}" +
        "三星大保底触发 -> ${get("三星大保底触发")}" +
        "四星大保底触发 -> ${get("四星大保底触发")}" +
        "五星大保底触发 -> ${get("五星大保底触发")}" +
        "抽卡总次数 -> ${get("抽卡总次数")}"

    }

    fun coverOf (detail: CardDrawDetail)
    {
        this["三星小保底次数"] = detail["三星小保底次数"].cint
        this["四星小保底次数"] = detail["四星小保底次数"].cint
        this["五星小保底次数"] = detail["五星小保底次数"].cint
        this["三星抽卡总次数"] = detail["三星抽卡总次数"].cint
        this["四星抽卡总次数"] = detail["四星抽卡总次数"].cint
        this["五星抽卡总次数"] = detail["五星抽卡总次数"].cint
        this["三星大保底触发"] = detail["三星大保底触发"].cbool
        this["四星大保底触发"] = detail["四星大保底触发"].cbool
        this["五星大保底触发"] = detail["五星大保底触发"].cbool
        this["抽卡总次数"] = detail["抽卡总次数"].cint
    }

    companion object
    {
        @JvmStatic
        fun deserialize (detail: Map<String, Any>): CardDrawDetail
        {
            val result = CardDrawDetail (detail["卡池名"].toString()).apply {
                this["三星小保底次数"] = detail["三星小保底次数"]!!
                this["四星小保底次数"] = detail["四星小保底次数"]!!
                this["五星小保底次数"] = detail["五星小保底次数"]!!
                this["三星抽卡总次数"] = detail["三星抽卡总次数"]!!
                this["四星抽卡总次数"] = detail["四星抽卡总次数"]!!
                this["五星抽卡总次数"] = detail["五星抽卡总次数"]!!
                this["三星大保底触发"] = detail["三星大保底触发"]!!
                this["四星大保底触发"] = detail["四星大保底触发"]!!
                this["五星大保底触发"] = detail["五星大保底触发"]!!
                this["抽卡总次数"] = detail["抽卡总次数"]!!

            }
            println(result)
            return result
        }
    }
}