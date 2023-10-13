package cn.gionrose.facered.genshinDrawcard2.api.card


import taboolib.common5.RandomList


/**
 * @description 星级池
 * @author facered
 * @date 2023/10/7 13:32
 */
class StarGradeContainer(val pool: CardPool, val grade: CardStarGrade)
{
    val cards = RandomList<Card> ()

    /**
     * 星级卡片容器中的每张卡片有且只有一张
     */
    fun addCard (card: Card, weight: Int)
    {
        cards.takeIf { !cards.values().contains(RandomList.Value(card, weight)) }?.apply {
            cards.add(card, weight)
        }
    }

    /**
     * 星级卡片容器的数量
     */
    fun size (): Int
    {
        return cards.size()
    }




}
