package cn.gionrose.facered.genshinDrawcard2.util

import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardStarGrade
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @description 玩家工具类
 * @author facered
 * @date 2023/10/11 23:42
 */

fun String.getPlayer (): Player?
{
    return Bukkit.getPlayer(this)
}

fun List<Card>.getMaxGrade (): CardStarGrade
{
    var maxGrade = CardStarGrade.THREE_STAR

    forEach {
        if (maxGrade < it.grade)
            maxGrade = it.grade
    }
    return maxGrade
}