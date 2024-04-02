package cn.gionrose.facered.genshinDrawcard2.internal.manager

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import com.skillw.pouvoir.api.manager.ConfigManager
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.util.toMap
import org.bukkit.configuration.file.FileConfiguration
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.ConfigFile
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import java.io.File


/**
 * @description 配置文件管理器实现
 * @author facered
 * @date 2023/10/7 0:13
 */
object GenshinDrawcard2ConfigManagerImpl: ConfigManager(GenshinDrawcard2) {

    override val priority = 0

    var debugMode: Boolean = false
    val debug: Boolean
        get() = debugMode || this["config"].getBoolean("options.debug")

    val databaseHost = Configuration.loadFromOther(this["config"], Type.YAML).getConfigurationSection("database")
    @JvmStatic
    fun debug(debug: () -> Unit) {
        if (this.debug) {
            debug.invoke()
        }
    }



}