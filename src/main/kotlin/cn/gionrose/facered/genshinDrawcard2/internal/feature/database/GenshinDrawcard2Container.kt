package cn.gionrose.facered.genshinDrawcard2.internal.feature.database

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.feature.database.UserBased
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * @description 数据库容器
 * @author facered
 * @date 2023/10/13 12:24
 */
internal object GenshinDrawcard2Container: UserBased {

    @JvmStatic
    val holder by lazy {
        Pouvoir.databaseManager.containerHolder(GenshinDrawcard2.configManager.databaseConfig)
    }

    @JvmStatic
    lateinit var container: UserBased

    @Awake(LifeCycle.ENABLE)
    fun loadContainer() {
        kotlin.runCatching {
            container = (holder?.container("抽卡详情", true) as? UserBased?)!!
        }.let {
            if (it.isFailure)
                taboolib.common.platform.function.warning("抽卡 玩家存储信息容器初始化失败!")
        }
    }
    override fun get(user: String, key: String): String? {
        return container[user, key]
    }

    override fun delete(user: String, key: String) {
        return container.delete(user, key)
    }

    override fun set(user: String, key: String, value: String?) {
        container[user, key] = value
    }

    override fun contains(user: String, key: String): Boolean {
        return container.contains(user, key)
    }
}