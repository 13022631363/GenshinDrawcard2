package cn.gionrose.facered.genshinDrawcard2.api.manager

import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardPool
import cn.gionrose.facered.genshinDrawcard2.api.card.StarGradeContainer
import com.skillw.pouvoir.api.manager.Manager
import taboolib.common5.RandomList

/**
 * @description 星级池容器管理器
 * @author facered
 * @date 2023/10/10 23:18
 */
abstract class StarGradeContainerManager: Manager {

    abstract fun registerContainer (gradeContainer: StarGradeContainer)

    abstract fun unregisterContainer (gradeContainer: StarGradeContainer)

    abstract fun releaseAllContainer (pool: CardPool)

    abstract fun getContainer (pool: CardPool): RandomList<StarGradeContainer>


    /**
     * 随机出的星级池容器
     */
    abstract fun randomStarGradeContainer (pool: CardPool): StarGradeContainer?

    /**
     *  随机出的 up card
     */
    abstract fun upRandomCard (container: StarGradeContainer): Card?

    /**
     *  随机出的 card
     */
    abstract fun randomCard (container: StarGradeContainer): Card?
}