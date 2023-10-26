package cn.gionrose.facered.genshinDrawcard2.internal.feature.compat

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import com.skillw.pouvoir.api.feature.realizer.BaseRealizer
import com.skillw.pouvoir.api.feature.realizer.BaseRealizerManager
import com.skillw.pouvoir.api.feature.realizer.component.Awakeable
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.common.util.unsafeLazy


/**
 * @description mythicMobs5.4 钩子
 * @author facered
 * @date 2023/10/25 10:20
 */
@AutoRegister
internal object MythicMobsVHook: BaseRealizer ("mythicMobsVHook"), Awakeable{

    override val file by lazy {
        GenshinDrawcard2.config.file!!
    }

    val defaultItem = ItemStack(Material.STICK)

    var isLoad = false


    override val manager: BaseRealizerManager by unsafeLazy {
        GenshinDrawcard2.realizerManager
    }

    override fun onEnable() {
        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null)
        {
            console().sendMessage(""" &6前置 &9[ &MythicMobsV &9] &6插件已加载... """)
            isLoad = true
        }
    }

    fun getMythicItem (name: String): ItemStack
    {
        defaultItem.takeIf { it.itemMeta != null }?.apply {
            val meta = itemMeta

            if (isLoad) meta!!.setDisplayName(" &4 提供的 MythicMobsV 物品 id &9[&f$name&9] &4不存在...")
            else meta!!.setDisplayName(" &4前置 &9[ &MythicMobsV &9] &4插件未加载 &9, &4请检查...")

            itemMeta = meta
        }

        if (!isLoad)
        {
            console().sendMessage(""" &4前置 &9[ &MythicMobsV &9] &4插件未加载 &9, &4请检查... """)
            return defaultItem.clone ()
        }

        return MythicBukkit.inst().itemManager.getItemStack(name) ?: defaultItem.clone ()
    }
}