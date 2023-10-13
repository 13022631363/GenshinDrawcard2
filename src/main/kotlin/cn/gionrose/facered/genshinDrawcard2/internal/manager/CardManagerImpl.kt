package cn.gionrose.facered.genshinDrawcard2.internal.manager

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.event.CardLoadEvent
import cn.gionrose.facered.genshinDrawcard2.api.manager.CardManager
import cn.gionrose.facered.genshinDrawcard2.internal.manager.GenshinDrawcard2ConfigManagerImpl.debug
import taboolib.common.platform.function.console
import taboolib.module.lang.sendError
import taboolib.module.lang.sendLang

/**
 * @description 卡片管理器实现
 * @author facered
 * @date 2023/10/6 22:41
 */
object CardManagerImpl: CardManager() {
    override val key = "CardManager"

    override val priority = 2

    override val subPouvoir = GenshinDrawcard2

    override fun registerCard(card: List<Card>) {
        card.forEach (::register)
    }

    override fun registerCard(card: Card) {
        register(card)
    }

    override fun unregisterCard(cardKeys: List<String>) {
        cardKeys.forEach (::remove)
    }

    override fun releaseAllCards() {
        clear ()
    }

    override fun getAllCards(): List<Card> {
        return ArrayList (this.values)
    }

    override fun getCard(cardName: String): Card? {
        return this[cardName]
    }

    //------------------------------------------------------------------------------------
    //                          卡片管理器的调用周期
    //------------------------------------------------------------------------------------

    override fun onEnable() {
        val cardLoadEvent = CardLoadEvent (ArrayList ())
        cardLoadEvent.call()
        if (cardLoadEvent.isCancelled)
            return
        releaseAllCards ()
        cardLoadEvent.allCards.apply(::registerCard)
    }

    override fun onReload() {
        onEnable()
    }




    //------------------------------------------------------------------------------------
    //                          重写父类
    //------------------------------------------------------------------------------------

    override fun register(value: Card) {

        if (values.contains(value)) return
        super.register(value)
        register(value.key, value)
        debug{
            console().sendLang("卡片_加载", value.key)
        }
    }

    override fun remove(key: String): Card? {
        val result = super.remove(key)

        result?.let { debug {
            console().sendLang("卡片_释放", key)
        }}

        return result
    }

    override fun get(key: String): Card? {

        val result = super.get(key)

        result ?: console().sendError("卡片_获取_失败", key)

        return result
    }

    override fun clear() {
            this.keys.forEach (::remove)
    }


}