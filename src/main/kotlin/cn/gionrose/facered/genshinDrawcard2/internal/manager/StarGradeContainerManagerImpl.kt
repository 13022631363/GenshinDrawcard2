package cn.gionrose.facered.genshinDrawcard2.internal.manager

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardPool
import cn.gionrose.facered.genshinDrawcard2.api.card.CardStarGrade
import cn.gionrose.facered.genshinDrawcard2.api.card.StarGradeContainer
import cn.gionrose.facered.genshinDrawcard2.api.manager.StarGradeContainerManager
import cn.gionrose.facered.genshinDrawcard2.internal.manager.GenshinDrawcard2ConfigManagerImpl.debug
import taboolib.common.platform.function.console
import taboolib.common5.RandomList
import taboolib.module.lang.sendError
import taboolib.module.lang.sendLang

/**
 * @description 星级池容器管理器实现
 * @author facered
 * @date 2023/10/10 23:20
 */
object StarGradeContainerManagerImpl: StarGradeContainerManager() {

    override val key = "StarGradeContainerManager"
    override val priority = 2
    override val subPouvoir = GenshinDrawcard2

    val containers = mutableMapOf<CardPool,RandomList<StarGradeContainer>>()


    override fun registerContainer(gradeContainer: StarGradeContainer) {
        val pool = gradeContainer.pool
        containers.computeIfAbsent(pool){RandomList()}.add(gradeContainer, when (gradeContainer.grade)
        {
            CardStarGrade.THREE_STAR -> pool.threeStarWeight
            CardStarGrade.FOUR_STAR -> pool.fourStarWeight
            CardStarGrade.FIVE_STAR -> pool.fiveStarWeight
        })
        debug {
            console().sendLang("星级卡片池_添加", pool.key, gradeContainer.grade.content)
        }
    }

    override fun unregisterContainer(gradeContainer: StarGradeContainer) {
        val pool = gradeContainer.pool
        containers[pool]?.let {
            it.remove(gradeContainer)
            debug {
                console().sendLang("星级卡片池_移除", pool.key, gradeContainer.grade.content)
            }
        }
    }

    override fun releaseAllContainer(pool: CardPool) {
        containers[pool]?.let {allContainer->
            allContainer.values().forEach {
                unregisterContainer( it.element)
            }
        }
    }

    override fun getContainer(pool: CardPool): RandomList<StarGradeContainer> {
        return containers.computeIfAbsent(pool){RandomList()}.apply {
           if (size() == 0)
           {
               registerContainer(StarGradeContainer(pool, CardStarGrade.THREE_STAR))
               registerContainer(StarGradeContainer(pool, CardStarGrade.FOUR_STAR))
               registerContainer(StarGradeContainer(pool, CardStarGrade.FIVE_STAR))
           }
        }
    }

    override fun randomStarGradeContainer(pool: CardPool): StarGradeContainer? {
        return containers.computeIfAbsent(pool){RandomList()}.random()
    }

    override fun randomCard(container: StarGradeContainer): Card? {
        return container.random()
    }

    override fun upRandomCard(container: StarGradeContainer): Card? {
        return container.upRandom ()
    }

    override fun registerCard(container: StarGradeContainer, card: Card, weight: Int) {
        container.addCard(card, weight)
    }


    //------------------------------------------------------------------------------------
    //                          星级池的拓展函数
    //------------------------------------------------------------------------------------
    private fun StarGradeContainer.random (): Card?
    {
        return cards.random()
    }

    private fun StarGradeContainer.addCard (card: Card, weight: Int)
    {
        cards.takeIf { !cards.values().contains(RandomList.Value(card, weight)) }?.apply {
            cards.add(card, weight)
        }
    }

    private fun StarGradeContainer.upRandom (): Card?
    {


            val result = RandomList<Card> ()
            cards.values().forEach {
                if (it.element.isUp)
                    result.add(it.element,it.index)
            }

            result.takeIf { it.size () == 0 }?.let {
                console().sendError("星级up卡片池_长度是0", pool.key, grade.content)
            }
            return result.random()
    }

}