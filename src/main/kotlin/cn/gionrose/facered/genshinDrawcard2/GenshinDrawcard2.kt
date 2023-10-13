package cn.gionrose.facered.genshinDrawcard2

import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardPool
import cn.gionrose.facered.genshinDrawcard2.api.card.CardStarGrade
import cn.gionrose.facered.genshinDrawcard2.api.event.CardLoadEvent
import cn.gionrose.facered.genshinDrawcard2.api.event.CardPoolLoadEvent
import cn.gionrose.facered.genshinDrawcard2.api.event.HandingOutCardEvent
import cn.gionrose.facered.genshinDrawcard2.api.manager.*
import cn.gionrose.facered.genshinDrawcard2.internal.feature.personal.PersonalDrawCardDetailData
import cn.gionrose.facered.genshinDrawcard2.internal.manager.GenshinDrawcard2ConfigManagerImpl
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.annotation.PouManager
import org.bukkit.Material
import taboolib.common.platform.Plugin
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submitAsync
import taboolib.module.chat.colored
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.buildItem
import taboolib.platform.util.onlinePlayers

object GenshinDrawcard2: Plugin(), SubPouvoir {

    override val key = "GenshinDrawcard2"

    override lateinit var managerData: ManagerData

    override val plugin by lazy {
        BukkitPlugin.getInstance()
    }


    @Config (migrate = true)
    lateinit var config: ConfigFile

    @JvmStatic
    @PouManager
    lateinit var cardManager: CardManager

    @JvmStatic
    @PouManager
    lateinit var configManager: GenshinDrawcard2ConfigManagerImpl

    @JvmStatic
    @PouManager
    lateinit var cardPoolManager: CardPoolManager

    @JvmStatic
    @PouManager
    lateinit var cardDrawDetailManager: CardDrawDetailManager

    @JvmStatic
    @PouManager
    lateinit var realizerManager: RealizerManager

    @JvmStatic
    @PouManager
    lateinit var starGradeContainerManager: StarGradeContainerManager

    @JvmStatic
    @PouManager
    lateinit var screenManager: ScreenManager

    @SubscribeEvent
    fun xxss (event: HandingOutCardEvent)
    {
        val player = event.player
        val cards = event.drawedCard

        cards.forEach {card ->
            if (card.key == "肥猪肚")
                player.inventory.addItem(buildItem(Material.PORKCHOP){
                    name = "肥猪肚"
                    this.originMeta?.let {
                        it.lore = listOf("&a恭喜你啊", "&6此法宝: &肥猪肚").colored()
                    }
                })

        }
    }
    @SubscribeEvent
    fun xxs (event: CardPoolLoadEvent)
    {
        val loadedCards = event.loadedCards
        val pool = CardPool.create("基础奖池") {}

        event.allCardPools.apply{
            add(pool)
        }

        loadedCards["肥牛"]?.let {
            cardPoolManager.registerCard(pool, it, 4)
        }
        loadedCards["肥鸡"]?.let {
            cardPoolManager.registerCard(pool, it, 4)
        }
        loadedCards["肥小鱼"]?.let {

            cardPoolManager.registerCard(pool, it, 5)
        }
        loadedCards["肥猪"]?.let {
            cardPoolManager.registerCard(pool, it, 12)
        }
        loadedCards["肥蟑螂"]?.let {
            cardPoolManager.registerCard(pool, it, 1)
        }
        loadedCards["肥小羊"]?.let {

            cardPoolManager.registerCard(pool, it, 5)
        }
        loadedCards["肥野牛"]?.let {
            cardPoolManager.registerCard(pool, it, 9)
        }
        loadedCards["肥野猪"]?.let {
            cardPoolManager.registerCard(pool, it, 10)
        }
        loadedCards["肥大排"]?.let {

            cardPoolManager.registerCard(pool, it, 1)
        }
        loadedCards["肥体胖"]?.let {
            cardPoolManager.registerCard(pool, it, 8)
        }
        loadedCards["肥猪蹄"]?.let {
            cardPoolManager.registerCard(pool, it, 4)
        }
        loadedCards["肥羊肚"]?.let {

            cardPoolManager.registerCard(pool, it, 7)
        }
        loadedCards["肥猪肚"]?.let {
            cardPoolManager.registerCard(pool, it, 10)
        }
        loadedCards["肥牛肚"]?.let {
            cardPoolManager.registerCard(pool, it, 10)
        }
        loadedCards["肥大肠"]?.let {

            cardPoolManager.registerCard(pool, it, 10)
        }
        loadedCards["肥小肠"]?.let {

            cardPoolManager.registerCard(pool, it, 4)
        }
        loadedCards["肥月饼"]?.let {
            cardPoolManager.registerCard(pool, it, 3)
        }
        loadedCards["肥蛋糕"]?.let {
            cardPoolManager.registerCard(pool, it, 1)
        }
        loadedCards["肥狮子"]?.let {

            cardPoolManager.registerCard(pool, it, 4)
        }
        loadedCards["肥苹果"]?.let {

            cardPoolManager.registerCard(pool, it, 6)
        }
        loadedCards["肥桃子"]?.let {
            cardPoolManager.registerCard(pool, it, 10)
        }
        loadedCards["肥橘子"]?.let {
            cardPoolManager.registerCard(pool, it, 3)
        }
        loadedCards["肥木鱼"]?.let {

            cardPoolManager.registerCard(pool, it, 3)
        }
        loadedCards["肥象拔蚌"]?.let {

            cardPoolManager.registerCard(pool, it, 6)
        }
        loadedCards["肥赤贝"]?.let {
            cardPoolManager.registerCard(pool, it, 6)
        }
        loadedCards["肥蜗牛"]?.let {
            cardPoolManager.registerCard(pool, it, 1)
        }
        loadedCards["肥暹罗"]?.let {

            cardPoolManager.registerCard(pool, it, 1)
        }

    }
    @SubscribeEvent
    fun xx (event: CardLoadEvent)
    {
        event.allCards.apply {
            add (Card.createCard("肥牛", buildItem(Material.BLACK_STAINED_GLASS_PANE) { name = "肥牛" }, true, CardStarGrade.getStarGrade(3)))
            add (Card.createCard("肥鸡", buildItem(Material.STICK) { name = "肥鸡" }, false, CardStarGrade.getStarGrade(3)))
            add (Card.createCard("肥小鱼", buildItem(Material.STICK) { name = "肥小鱼" }, true,CardStarGrade.getStarGrade(3)))
            add (Card.createCard("肥猪", buildItem(Material.STICK) { name = "肥猪" }, true, CardStarGrade.getStarGrade(3)))
            add (Card.createCard("肥蟑螂", buildItem(Material.STICK) { name = "肥蟑螂" }, false, CardStarGrade.getStarGrade(3)))
            add (Card.createCard("肥小羊", buildItem(Material.STICK) { name = "肥小羊" }, false, CardStarGrade.getStarGrade(3)))
            add (Card.createCard("肥野牛", buildItem(Material.STICK) { name = "肥野牛" }, false, CardStarGrade.getStarGrade(3)))
            add (Card.createCard("肥野猪", buildItem(Material.STICK) { name = "肥野猪" }, false, CardStarGrade.getStarGrade(3)))
            add (Card.createCard("肥大排", buildItem(Material.STICK) { name = "肥大排" }, true, CardStarGrade.getStarGrade(3)))
            add (Card.createCard("肥体胖", buildItem(Material.STICK) { name = "肥体胖" }, false, CardStarGrade.getStarGrade(4)))
            add (Card.createCard("肥猪蹄", buildItem(Material.STICK) { name = "肥猪蹄" }, false, CardStarGrade.getStarGrade(4)))
            add (Card.createCard("肥羊肚", buildItem(Material.STICK) { name = "肥羊肚" }, true,CardStarGrade.getStarGrade(4)))
            add (Card.createCard("肥猪肚", buildItem(Material.STICK) { name = "肥猪肚" }, true, CardStarGrade.getStarGrade(4)))
            add (Card.createCard("肥牛肚", buildItem(Material.STICK) { name = "肥牛肚" }, true, CardStarGrade.getStarGrade(4)))
            add (Card.createCard("肥大肠", buildItem(Material.STICK) { name = "肥大肠" }, false,CardStarGrade.getStarGrade(4)))
            add (Card.createCard("肥小肠", buildItem(Material.STICK) { name = "肥小肠" }, false, CardStarGrade.getStarGrade(4)))
            add (Card.createCard("肥月饼", buildItem(Material.STICK) { name = "肥月饼" }, false, CardStarGrade.getStarGrade(4)))
            add (Card.createCard("肥蛋糕", buildItem(Material.STICK) { name = "肥蛋糕" }, false,CardStarGrade.getStarGrade(4)))
            add (Card.createCard("肥狮子", buildItem(Material.STICK) { name = "肥狮子" }, false, CardStarGrade.getStarGrade(5)))
            add (Card.createCard("肥苹果", buildItem(Material.STICK) { name = "肥苹果" }, false, CardStarGrade.getStarGrade(5)))
            add (Card.createCard("肥橘子", buildItem(Material.STICK) { name = "肥橘子" }, false,CardStarGrade.getStarGrade(5)))
            add (Card.createCard("肥桃子", buildItem(Material.STICK) { name = "肥桃子" }, false, CardStarGrade.getStarGrade(4)))
            add (Card.createCard("肥木鱼", buildItem(Material.STICK) { name = "肥木鱼" }, false, CardStarGrade.getStarGrade(5)))
            add (Card.createCard("肥象拔蚌", buildItem(Material.STICK) { name = "肥象拔蚌" }, false,CardStarGrade.getStarGrade(5)))
            add (Card.createCard("肥赤贝", buildItem(Material.STICK) { name = "肥赤贝" }, true, CardStarGrade.getStarGrade(5)))
            add (Card.createCard("肥蜗牛", buildItem(Material.STICK) { name = "肥蜗牛" }, true, CardStarGrade.getStarGrade(5)))
            add (Card.createCard("肥暹罗", buildItem(Material.STICK) { name = "肥暹罗" }, true,CardStarGrade.getStarGrade(5)))
        }
    }


    override fun onEnable() {
        enable()
        println(plugin.server.version)
        submitAsync (period = 1200){
            onlinePlayers.forEach {
                PersonalDrawCardDetailData.pushAttrData(it)
            }
        }
    }

    override fun onActive() {
        active()
    }

    override fun onLoad() {

        load()
    }

    override fun onDisable() {
        disable()
    }

}