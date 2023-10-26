package cn.gionrose.facered.genshinDrawcard2.internal.manager

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.Card
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
    override fun addCount(uuid: UUID, detailName: String, count: Int) {
        val amount = this[uuid][detailName].cint
        this[uuid][detailName] = amount + count
    }

    override fun clearCount(uuid: UUID, detailName: String) {
        val amount = this[uuid][detailName].cint
        this[uuid][detailName] = 0
    }

    override fun setTriggerBigGuarantee(uuid: UUID, detailName: String, isTrigger: Boolean) {
        this[uuid][detailName] = isTrigger
    }

    override fun addCardRecord(uuid: UUID, card: Card) {
        val record = this[uuid]["抽卡记录"] as MutableList<Card>

        record.add(card)
    }

    override fun getCount(uuid: UUID, detailName: String): Int {
        return this[uuid][detailName].cint
    }

    override fun isTriggerBigGuarantee(uuid: UUID, detailName: String): Boolean {
        return this[uuid][detailName].cbool
    }

    override fun getCardRecord(uuid: UUID): List<Card> {
        return this[uuid]["抽卡记录"] as List<Card>
    }

    override fun unregisterDetail(uuid: UUID) {
        remove(uuid)
    }

    //------------------------------------------------------------------------------------
    //                          重写父类
    //------------------------------------------------------------------------------------

    override fun get(key: UUID): CardDrawDetail {

        return computeIfAbsent(key){CardDrawDetail ()}
    }

    override fun remove(key: UUID): CardDrawDetail {

        val result = this[key]
        result.takeIf {it.isNotEmpty()}?.let { debug{
            console().sendLang("玩家抽卡详情_移除", key.player()!!.name)
        }}
        super.remove(key, this[key])

        return result
    }
}