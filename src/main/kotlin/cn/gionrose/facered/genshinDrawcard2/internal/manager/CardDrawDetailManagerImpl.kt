package cn.gionrose.facered.genshinDrawcard2.internal.manager

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.CardDrawDetail
import cn.gionrose.facered.genshinDrawcard2.api.manager.CardDrawDetailManager
import cn.gionrose.facered.genshinDrawcard2.internal.manager.GenshinDrawcard2ConfigManagerImpl.debug
import com.skillw.pouvoir.util.player
import taboolib.common.platform.function.console
import taboolib.common5.cbool
import taboolib.common5.cint
import taboolib.module.lang.sendLang
import java.util.*

/**
 * @description 抽卡详情管理器实现
 * @author facered
 * @date 2023/10/7 19:53
 */
object CardDrawDetailManagerImpl: CardDrawDetailManager() {

    override val key = "CardDrawDetailManager"

    override val priority = 2

    override val subPouvoir = GenshinDrawcard2
    override fun addCount(uuid: UUID, poolName: String, detailName: String, count: Int) {
        this[uuid].forEach {
            if (it.key == poolName)
            {
                val amount = it[detailName].cint
                it[detailName] = amount + count
            }
        }
    }

    override fun clearCount(uuid: UUID, poolName: String, detailName: String) {
        this[uuid].forEach {
            if (it.key == poolName)
            {
                it[detailName] = -1
            }
        }
    }

    override fun setTriggerBigGuarantee(uuid: UUID, poolName: String, detailName: String, isTrigger: Boolean) {
        this[uuid].forEach {
            if (it.key == poolName)
            {
                it[detailName] = isTrigger
            }
        }
    }

    override fun getCount(uuid: UUID, poolName: String, detailName: String): Int {
        this[uuid].forEach {
            if (it.key == poolName)
            {
                return it[detailName].cint
            }
        }
        return -1
    }

    override fun isTriggerBigGuarantee(uuid: UUID, poolName: String, detailName: String): Boolean {
        this[uuid].forEach {
            if (it.key == poolName)
            {
                return it[detailName].cbool
            }
        }
        return false
    }

    override fun unregisterDetail(uuid: UUID) {
        remove(uuid)
    }

    //------------------------------------------------------------------------------------
    //                          重写父类
    //------------------------------------------------------------------------------------

    override fun get(key: UUID): MutableList<CardDrawDetail> {

        return computeIfAbsent(key){ mutableListOf()}
    }

    override fun remove(key: UUID): MutableList<CardDrawDetail> {

        val result = this[key]
        result.takeIf {it.isNotEmpty()}?.let { debug{
            console().sendLang("玩家抽卡详情_移除", key.player()!!.name)
        }}
        super.remove(key, this[key])

        return result
    }
}