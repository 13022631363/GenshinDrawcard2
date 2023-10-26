package cn.gionrose.facered.genshinDrawcard2.api.manager

import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardDrawDetail
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.BaseMap
import java.util.*

/**
 * @description 抽卡详情管理器
 * @author facered
 * @date 2023/10/7 23:36
 */
abstract class CardDrawDetailManager: BaseMap<UUID, CardDrawDetail> (), Manager {

    abstract fun addCount (uuid: UUID, detailName: String, count: Int)

    abstract fun clearCount (uuid: UUID, detailName: String)

    abstract fun setTriggerBigGuarantee (uuid: UUID, detailName: String, isTrigger: Boolean)

    abstract fun addCardRecord (uuid: UUID, card: Card)

    abstract fun getCount (uuid: UUID, detailName: String): Int

    abstract fun isTriggerBigGuarantee (uuid: UUID, detailName: String): Boolean

    abstract fun getCardRecord (uuid: UUID): List<Card>

    abstract fun unregisterDetail (uuid: UUID)
}