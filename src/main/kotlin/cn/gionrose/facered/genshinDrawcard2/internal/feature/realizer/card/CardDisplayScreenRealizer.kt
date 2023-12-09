package cn.gionrose.facered.genshinDrawcard2.internal.feature.realizer.card

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardPool
import cn.gionrose.facered.genshinDrawcard2.api.event.BillingScreenCloseEvent
import cn.gionrose.facered.genshinDrawcard2.api.event.BillingScreenOpenEvent
import com.skillw.pouvoir.api.feature.realizer.BaseRealizer
import com.skillw.pouvoir.api.feature.realizer.BaseRealizerManager
import com.skillw.pouvoir.api.feature.realizer.component.Awakeable
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import org.bukkit.entity.Player
import taboolib.common.util.unsafeLazy
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked

/**
 * @description 抽卡预览 / 结算界面实现器
 * @author facered
 * @date 2023/10/12 21:58
 */
@AutoRegister
internal object CardDisplayScreenRealizer: BaseRealizer("展示界面实现器"), Awakeable {
    override val file by lazy {
        GenshinDrawcard2.config.file!!
    }
    override val manager: BaseRealizerManager by unsafeLazy {
        GenshinDrawcard2.realizerManager
    }

    fun show (player: Player, pool: CardPool, drawedCards: MutableList<Card>, screenName: String)
    {

        val allScreen = GenshinDrawcard2.screenManager.parseLayout(pool, screenName)
        var billingScreen = allScreen[0]

        if (screenName == "结算界面")
        {
            BillingScreenOpenEvent(player).call()
            allScreen.forEach {
                if (drawedCards.size == it.pageSize)
                    billingScreen = it
            }
        }

        player.openMenu<Linked<Card>>(billingScreen.title)
        {
            rows(billingScreen.row)

            handLocked(true)

            slots(billingScreen.slots)

            elements { drawedCards }

            onGenerate { _, element, _, _ ->
                element.source
            }

            billingScreen.nextButton.forEach { (slot, card) ->
                setNextPage(slot){_, has ->
                    if (has)
                        card.source
                    else
                        billingScreen.replaceNextButton[slot]!!.source
                }
            }

            billingScreen.prevButton.forEach { (slot, card) ->
                setPreviousPage(slot){_, has ->
                    if (has)
                        card.source
                    else
                        billingScreen.replacePrevButton[slot]!!.source
                }
            }

            billingScreen.fixedSettingButtons.forEach { (slot, card) ->
                set(slot, card.source)
            }

            onClose{
                if(screenName == "结算界面")
                    BillingScreenCloseEvent (player, pool.key).call()
                GenshinDrawcard2.screenManager.releaseSlots(pool, "结算界面")
            }
        }

    }

}