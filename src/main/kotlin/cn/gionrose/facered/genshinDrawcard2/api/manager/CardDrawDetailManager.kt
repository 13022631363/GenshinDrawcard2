package cn.gionrose.facered.genshinDrawcard2.api.manager

import cn.gionrose.facered.genshinDrawcard2.api.card.CardDrawDetail
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.BaseMap
import java.util.*

/**
 * @description 抽卡详情管理器
 * @author facered
 * @date 2023/10/7 23:36
 */
abstract class CardDrawDetailManager: BaseMap<UUID, MutableList<CardDrawDetail>> (), Manager {

    abstract fun addCount (uuid: UUID, poolName: String, detailName: String, count: Int)

    abstract fun clearCount (uuid: UUID, poolName: String, detailName: String)

    abstract fun setTriggerBigGuarantee (uuid: UUID, poolName: String, detailName: String, isTrigger: Boolean)

    abstract fun getCount (uuid: UUID, poolName: String, detailName: String): Int

    abstract fun isTriggerBigGuarantee (uuid: UUID, poolName: String, detailName: String): Boolean

    abstract fun unregisterDetail (uuid: UUID)
}