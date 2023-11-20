package cn.gionrose.facered.genshinDrawcard2

//import cn.gionrose.facered.genshinDrawcard2.internal.feature.personal.PersonalDrawCardDetailData
import cn.gionrose.facered.genshinDrawcard2.api.manager.*
import cn.gionrose.facered.genshinDrawcard2.internal.feature.database.GenshinDrawCard2Database
import cn.gionrose.facered.genshinDrawcard2.internal.manager.GenshinDrawcard2ConfigManagerImpl
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.annotation.PouManager
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.platform.BukkitPlugin

@RuntimeDependency(value = "mysql:mysql-connector-java:5.1.6")
object GenshinDrawcard2: Plugin(), SubPouvoir {

    override val key = "GenshinDrawcard2"

    override lateinit var managerData: ManagerData

    override val plugin by lazy {
        BukkitPlugin.getInstance()
    }


    @Config (migrate = true)
    lateinit var config: ConfigFile

    @JvmStatic
    @PouManager
    lateinit var cardManager: CardManager

    @JvmStatic
    @PouManager
    lateinit var configManager: GenshinDrawcard2ConfigManagerImpl

    @JvmStatic
    @PouManager
    lateinit var cardPoolManager: CardPoolManager

    @JvmStatic
    @PouManager
    lateinit var cardDrawDetailManager: CardDrawDetailManager

    @JvmStatic
    @PouManager
    lateinit var realizerManager: RealizerManager

    @JvmStatic
    @PouManager
    lateinit var starGradeContainerManager: StarGradeContainerManager

    @JvmStatic
    @PouManager
    lateinit var screenManager: ScreenManager



    override fun onEnable() {
        enable()
        GenshinDrawCard2Database.createTable()
    }

    override fun onActive() {
        active()
    }

    override fun onLoad() {

        load()
    }

    override fun onDisable() {
        disable()
    }

}