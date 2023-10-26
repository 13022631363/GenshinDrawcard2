package cn.gionrose.facered.genshinDrawcard2.internal.feature.realizer.card

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardPool
import cn.gionrose.facered.genshinDrawcard2.api.event.HandingOutCardEvent
import cn.gionrose.facered.genshinDrawcard2.util.getMaxGrade
import com.skillw.pouvoir.api.feature.realizer.BaseRealizer
import com.skillw.pouvoir.api.feature.realizer.BaseRealizerManager
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.common.util.unsafeLazy
import taboolib.module.lang.sendLang
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked

/**
 * @description 抽卡展示动画界面
 * @author facered
 * @date 2023/10/10 20:58
 */

@AutoRegister
internal object CardAnimationScreenRealizer: BaseRealizer ("动画界面实现器") {

    override val file by lazy {
        GenshinDrawcard2.config.file!!
    }
    override val manager: BaseRealizerManager by unsafeLazy {
        GenshinDrawcard2.realizerManager
    }


    fun show (player: Player, pool: CardPool, drawedCards: MutableList<Card>)
    {
        val maxGrade = drawedCards.getMaxGrade()
        var displayScreenName: String? = null
        val allScreen = GenshinDrawcard2.screenManager[pool]
        allScreen?.forEach { (screenName, _) ->
            if (screenName.contains (maxGrade.content))
                displayScreenName = screenName
        }

        allScreen ?: return console().sendLang("界面_未配置这个奖池的界面", pool.key)
        displayScreenName ?: return console().sendLang("界面_配置的展示动画界面_名字未带有星级", pool.key, maxGrade)

        val displayScreen = GenshinDrawcard2.screenManager.parseLayout(pool, displayScreenName!!)

        var index = 0
        val task = submit(period = pool.period) {

            if (index == displayScreen.size) {
                this.cancel()
                (GenshinDrawcard2.realizerManager["展示界面实现器"] as CardDisplayScreenRealizer).show(player,pool, drawedCards, "结算界面" )

                HandingOutCardEvent (player, drawedCards).call()
                return@submit
            }

            player.openMenu<Linked<Card>>(displayScreen[index].title)
            {
                rows(displayScreen[index].row)

                handLocked(true)

                displayScreen[index].fixedSettingButtons.forEach { (slot, card) ->
                    set(slot, card.source)
                }



                onClick { event ->
                    if (event.clickEvent().isRightClick && event.rawSlot == -999) {
                        index = displayScreen.size
                        event.clickEvent().view.close()
                    }
                }

            }
            index++
        }



    }


}