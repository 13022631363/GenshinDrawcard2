package cn.gionrose.facered.genshinDrawcard2.internal.feature.compat

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import com.skillw.pouvoir.api.feature.realizer.BaseRealizer
import com.skillw.pouvoir.api.feature.realizer.component.Awakeable
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.manager.ItemManager
import taboolib.common.platform.function.console

/**
 * @description neigeItems 钩子
 * @author facered
 * @date 2023/10/25 10:20
 */
@AutoRegister
internal object NeigeItemHook:  BaseRealizer ("neigeItemHook"), Awakeable{

    var isLoad = false

    val defaultItem = ItemStack(Material.STICK)

    override val file by lazy {
        GenshinDrawcard2.config.file!!
    }

    override val manager = GenshinDrawcard2.realizerManager


    override fun onEnable() {
        if (Bukkit.getPluginManager().getPlugin("NeigeItems") != null)
        {
            console().sendMessage(""" &6前置 &9[ &fNeigeItems &9] &6插件已加载... """)
            isLoad = true
        }
    }

    /**
     *  如果提供的 id 获取不到 neige 物品就将初始化好的木棍返回
     *  木棍的展示名进行信息提示
     */
    fun getNeigeItem (id: String): ItemStack
    {
        defaultItem.takeIf { it.itemMeta != null }?.apply {
            val meta = itemMeta

            if (isLoad) meta!!.setDisplayName(" &4 提供的 neige 物品 id &9[&f$id&9] &4不存在...")
            else meta!!.setDisplayName(" &4前置 &9[ &fNeigeItems &9] &4插件未加载 &9, &4请检查...")

            itemMeta = meta
        }

        if (!isLoad)
        {
            console().sendMessage(""" &4前置 &9[ &fNeigeItems &9] &4插件未加载 &9, &4请检查... """)
            return defaultItem.clone ()
        }

        return ItemManager.getItemStack(id)?: defaultItem.clone ()

    }

}