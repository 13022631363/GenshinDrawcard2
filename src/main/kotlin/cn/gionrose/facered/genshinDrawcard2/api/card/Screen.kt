package cn.gionrose.facered.genshinDrawcard2.api.card

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import com.skillw.pouvoir.api.plugin.map.component.Registrable

/**
 * @description 界面
 * @author facered
 * @date 2023/10/10 22:36
 */
class Screen (val title: String, val pageSize: Int, val row: Int, val layout: String, override val key: String, val pool: CardPool): Registrable<String>
{
    /**
     * 可用的格子
     */
    val slots = arrayListOf<Int>()

    /**
     * 前一页
     */
    val prevButton = mutableMapOf<Int, Card>()

    val replacePrevButton = mutableMapOf<Int, Card>()

    /**
     * 后一页
     */
    val nextButton = mutableMapOf<Int, Card>()

    val replaceNextButton = mutableMapOf<Int, Card>()

    /**
     * 固定设置物品 (格子下标  卡片名)
     */
    val fixedSettingButtons = mutableMapOf<Int, Card>()

    override fun register() {
        GenshinDrawcard2.screenManager.registerScreen (this)
    }

}