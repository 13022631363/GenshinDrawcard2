package cn.gionrose.facered.genshinDrawcard2.api.manager

import cn.gionrose.facered.genshinDrawcard2.api.card.CardPool
import cn.gionrose.facered.genshinDrawcard2.api.card.Screen
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.BaseMap

/**
 * @description 界面管理器
 * @author facered
 * @date 2023/10/11 19:31
 */
abstract class ScreenManager: BaseMap<CardPool, MutableMap<String, MutableList<Screen>>>() ,Manager {

    /**
     * 往 screenManager 中指定 pool 注册一个 screen
     */
    abstract fun registerScreen(pool: CardPool, screen: Screen)

    /**
     * 从 screenManager 中指定 pool 注销一个 screen
     */
    abstract fun unregisterScreen (pool: CardPool, screenName: String)

    /**
     * screenManager 中指定 pool 释放所有 screen
     */
    abstract fun releaseAllScreen (pool: CardPool)

    /**
     * 从 screenManager 中指定 pool 获取 screen
     */
    abstract fun getScreen (pool: CardPool, screenName: String): List<Screen>

    /**
     * 解析排版
     */
    abstract fun parseLayout (pool: CardPool, screenName: String): List<Screen>

    /**
     * 从 screenManager 中指定 pool 清楚 screen 的 所有slots
     */
    abstract fun releaseSlots (pool: CardPool, screenName: String)

}