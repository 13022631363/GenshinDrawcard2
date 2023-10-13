package cn.gionrose.facered.genshinDrawcard2.internal.manager

import cn.gionrose.facered.genshinDrawcard2.api.manager.RealizerManager
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * @description 实现管理器实现
 * @author facered
 * @date 2023/10/10 20:52
 */
object RealizerManagerImpl: RealizerManager () {
    override val priority = 999

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        onDisable()
    }

    override fun onReload() {

    }
}