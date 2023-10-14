package cn.gionrose.facered.genshinDrawcard2.internal.command

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardStarGrade
import cn.gionrose.facered.genshinDrawcard2.internal.feature.realizer.card.CardAnimationScreenRealizer
import cn.gionrose.facered.genshinDrawcard2.internal.feature.realizer.card.CardDisplayScreenRealizer
import cn.gionrose.facered.genshinDrawcard2.internal.feature.realizer.card.DrawCardRealizer
import cn.gionrose.facered.genshinDrawcard2.util.getPlayer
import com.skillw.pouvoir.util.soundSuccess
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.common5.cint
import taboolib.platform.util.onlinePlayers
import taboolib.platform.util.sendLang

/**
 * @description 命令
 * @author facered
 * @date 2023/10/9 14:58
 */
@CommandHeader (name = "drawcard", permission = "genshindrawcard2.command")
internal object GenshinDrawcard2Command {
    @CommandBody(permission = "genshindrawcard2.command.reload")
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            (sender as? Player?)?.soundSuccess()
            sender.sendLang("命令_重载")
            GenshinDrawcard2.reload()
        }
    }

    @CommandBody (permission = "genshindrawcard2.command.preview")
    val preview = subCommand {

        dynamic("玩家名") {
            suggestion<CommandSender> { _, _ ->
                onlinePlayers.map { it.name }
            }
            dynamic("卡片池名") {
                suggestion<Player>{_, _ ->
                    GenshinDrawcard2.cardPoolManager.values.map { it.key }
                }
                dynamic("星级") {
                    suggestion<Player>{_, _ ->
                        CardStarGrade.values().map { it.level.toString() }
                    }
                    execute<Player>{_, context, _ ->
                        val pool = GenshinDrawcard2.cardPoolManager[context["卡片池名"]]!!
                        GenshinDrawcard2.starGradeContainerManager.getContainer(pool).values().forEach { container ->
                            if (container.element.grade == CardStarGrade.getStarGrade(context["星级"].cint))
                            {
                                val cards = container.element.cards.values().map { it.element }.toMutableList()
                                val cardDisplayScreenRealizer =
                                    GenshinDrawcard2.realizerManager["展示界面实现器"]!! as CardDisplayScreenRealizer
                                cardDisplayScreenRealizer.show(context["玩家名"].getPlayer()!!,pool, cards, "预览界面" )
                                return@forEach
                            }
                        }
                    }
                }
            }
        }
    }

    @CommandBody(permission = "genshindrawcard2.command.draw")
    val draw = subCommand {

        dynamic("玩家名") {
            suggestion<CommandSender>{ _, _ ->
                onlinePlayers.map { it.name }
            }
            dynamic("卡片池名") {
                suggestion<CommandSender>{_, _ ->
                    GenshinDrawcard2.cardPoolManager.values.map { it.key }
                }
                dynamic("抽卡次数") {
                   execute<CommandSender> { _, context, _ ->
                       context["玩家名"].getPlayer()?.let { player ->

                           val drawedCards = mutableListOf<Card>()

                           for (count in 0 until context["抽卡次数"].toInt ())
                           {

                               (GenshinDrawcard2.realizerManager["抽卡实现器"] as DrawCardRealizer).draw("基础奖池", player.uniqueId)?.let {
                                   drawedCards.add(it)
                                   println("抽卡结果 => ${it.key}")
                               }
                           }

                           (GenshinDrawcard2.realizerManager["动画界面实现器"] as CardAnimationScreenRealizer).show(player, GenshinDrawcard2.cardPoolManager[context["卡片池名"]]!!, drawedCards)



                           player.sendMessage("抽卡...")

                           GenshinDrawcard2.cardDrawDetailManager.apply {
                               "${player.name} => 抽卡总次数: ${getCount(player.uniqueId, "抽卡总次数")}".apply (player::sendMessage)
                           }
                       }

                   }
                }
            }
        }

    }
    @CommandBody(permission = "genshindrawcard2.command.debug")
    val debug = subCommand {
        execute<CommandSender> { sender, _, _ ->
            (sender as? Player?)?.soundSuccess()
            GenshinDrawcard2.configManager.debugMode = !GenshinDrawcard2.configManager.debugMode
            sender.sendLang("command-debug-${if (GenshinDrawcard2.configManager.debugMode) "on" else "off"}")
        }
    }
}