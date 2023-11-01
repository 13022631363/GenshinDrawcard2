package cn.gionrose.facered.genshinDrawcard2.internal.manager

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardPool
import cn.gionrose.facered.genshinDrawcard2.api.card.Screen
import cn.gionrose.facered.genshinDrawcard2.api.event.ScreenLoadEvent
import cn.gionrose.facered.genshinDrawcard2.api.manager.ScreenManager
import cn.gionrose.facered.genshinDrawcard2.internal.manager.GenshinDrawcard2ConfigManagerImpl.debug
import org.bukkit.Material
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * @description  界面管理器实现
 * @author facered
 * @date 2023/10/11 19:34
 */
object ScreenManagerImpl: ScreenManager () {

    override val key = "ScreenManager"

    override val priority = 4

    override val subPouvoir = GenshinDrawcard2
    override fun registerScreen(screen: Screen) {
        computeIfAbsent(screen.pool) { mutableMapOf() }.computeIfAbsent(screen.key){ mutableListOf()}.add(screen)
        debug{
            console().sendLang("界面_添加", screen.key, screen.pool.key)
        }
    }

    override fun unregisterScreen(pool: CardPool, screenName: String) {
        this.forEach { itPool, screens ->
            if (pool.key == itPool.key)
                if (this.remove(itPool) != null)debug {
                    console().sendLang("界面_移除", screenName, pool.key)
                }
        }
    }

    override fun releaseAllScreen(pool: CardPool) {
        this.values.forEach {allScreen->
            allScreen.keys.forEach {
                unregisterScreen(pool, it)
            }
        }
    }

    override fun getScreen(pool: CardPool, screenName: String): List<Screen> {
        val result = mutableListOf<Screen>()

        this[pool]?.also {allScreen ->
            allScreen[screenName]?.also {
                result.addAll (it)
            } ?: debug {
                console().sendLang("界面_获取_失败_卡池不存在或没东西", screenName, pool.key)
            }
        } ?: debug {
            console().sendLang("界面_获取_失败_卡池不存在或没东西", screenName, pool.key)
        }

        return result
    }

    override fun parseLayout(pool: CardPool, screenName: String): List<Screen> {
        return getScreen(pool, screenName).onEach {it.parseLayout()}
    }

    override fun releaseSlots(pool: CardPool, screenName: String) {
        getScreen(pool, screenName).onEach {it.releaseSlots()}
    }

    override fun createScreen(pool: CardPool, title: String, row: Int, layout: String, key: String): Screen {
        return Screen (title, row, layout, key, pool)
    }


    private fun screenLayoutGetCardFromCardManager (screenName: String, needGetCardName : String): Card?
    {
        val result = GenshinDrawcard2.cardManager.getCard(needGetCardName)
        result ?: console().sendLang("界面_排版_卡片_获取_失败", screenName, needGetCardName)
        return result
    }

    //------------------------------------------------------------------------------------
    //                          Screen 的拓展函数
    //------------------------------------------------------------------------------------

    private fun Screen.parseLayout ()
    {
        val slotItem = this.layout.split(",")
        this.slots.clear()
        for (index in slotItem.indices)
        {
            val name = slotItem[index]

            if(name.contains("card"))
            {

                this.slots.add(index)
            }else if(name.contains("next||"))
            {
                //nextButton
                screenLayoutGetCardFromCardManager(this.key, name.split("||")[1])?.also { this.nextButton[index] = it }
                //replaceNextButton
                screenLayoutGetCardFromCardManager(this.key, name.split("||")[2])?.also { this.replaceNextButton[index] = it }

            }else if(name.contains("prev||"))
            {
                //prevButton
                screenLayoutGetCardFromCardManager(this.key, name.split("||")[1])?.also { this.prevButton[index] = it }
                //replacePrevButton
                screenLayoutGetCardFromCardManager(this.key, name.split("||")[2])?.also { this.replacePrevButton[index] = it }
            }else if (name.isBlank() || name.isEmpty())
            {

            }else
                screenLayoutGetCardFromCardManager(this.key, name)?.let { this.fixedSettingButtons[index] = it }
        }
    }

    private fun Screen.releaseSlots ()
    {
        this.slots.clear()
    }

    //------------------------------------------------------------------------------------
    //                          重写父类
    //------------------------------------------------------------------------------------



    //------------------------------------------------------------------------------------
    //                          临时
    //------------------------------------------------------------------------------------

    override fun onActive() {
        ScreenLoadEvent ().call()
        var screenManager = GenshinDrawcard2.screenManager
        Material.GRAY_STAINED_GLASS_PANE
//        GenshinDrawcard2.screenManager.apply {
//            registerScreen(pool, Screen("1", 6,"肥牛", "三星_展示动画", pool))
//            registerScreen(pool, Screen("2", 6,"肥牛,肥牛", "三星_展示动画", pool))
//            registerScreen(pool, Screen("3", 6,"肥牛,肥牛,肥牛", "三星_展示动画", pool))
//            registerScreen(pool, Screen("-", 6,"肥牛,肥牛", "四星_展示动画", pool))
//            registerScreen(pool, Screen("--", 6,"肥牛,肥牛,肥牛,肥牛", "四星_展示动画", pool))
//            registerScreen(pool, Screen("---", 6,"肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛", "四星_展示动画", pool))
//            registerScreen(pool, Screen(">", 6,"肥牛,肥牛,肥牛,肥牛", "五星_展示动画", pool))
//            registerScreen(pool, Screen(">>", 6,"肥牛,肥牛,肥牛,肥牛,肥牛,肥牛", "五星_展示动画", pool))
//            registerScreen(pool, Screen(">>>", 6,"肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛", "五星_展示动画", pool))
//
//            registerScreen(pool, Screen("结算界面", 6,
//                "肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛," +
//                    "肥牛,card,card,card,card,card,card,card,肥牛," +
//                    "肥牛,card,card,card,card,card,card,card,肥牛," +
//                    "肥牛,card,card,card,card,card,card,card,肥牛," +
//                    "肥牛,card,card,card,card,card,card,card,肥牛," +
//                    "prev||肥猪||肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,next||肥猪||肥牛", "结算界面", pool))
//            registerScreen(Screen("预览界面", 6,
//                "肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛," +
//                        "肥牛,card,card,card,card,card,card,card,肥牛," +
//                        "肥牛,card,card,card,card,card,card,card,肥牛," +
//                        "肥牛,card,card,card,card,card,card,card,肥牛," +
//                        "肥牛,card,card,card,card,card,card,card,肥牛," +
//                        "prev||肥猪||肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,next||肥猪||肥牛", "预览界面", pool))
//            registerScreen(pool, Screen("抽卡记录界面", 6,
//                "肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛," +
//                        "肥牛,card,card,card,card,card,card,card,肥牛," +
//                        "肥牛,card,card,card,card,card,card,card,肥牛," +
//                        "肥牛,card,card,card,card,card,card,card,肥牛," +
//                        "肥牛,card,card,card,card,card,card,card,肥牛," +
//                        "prev||肥猪||肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,肥牛,next||肥猪||肥牛", "抽卡记录", pool))
//        }

    }

    override fun onReload() {
        GenshinDrawcard2.cardPoolManager.forEach { (_, pool) ->
            GenshinDrawcard2.screenManager.releaseAllScreen(pool)
        }

        onActive()
    }




}