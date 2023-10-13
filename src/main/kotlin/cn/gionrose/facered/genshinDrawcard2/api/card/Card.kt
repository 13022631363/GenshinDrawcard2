package cn.gionrose.facered.genshinDrawcard2.api.card

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.CardStarGrade.THREE_STAR
import cn.gionrose.facered.genshinDrawcard2.util.deserializeF
import cn.gionrose.facered.genshinDrawcard2.util.serializeF
import com.skillw.pouvoir.api.plugin.map.component.Registrable
import org.bukkit.inventory.ItemStack
import taboolib.common5.cbool
import taboolib.library.xseries.XMaterial
import taboolib.platform.util.buildItem

/**
 * @description 卡片
 * @author facered
 * @date 2023/10/6 22:19
 */
class Card private constructor(
    override val key: String,
    val source: ItemStack,
    val isUp: Boolean = false,
    val grade: CardStarGrade = THREE_STAR
): Registrable<String>, Comparable<CardStarGrade> {

    fun clone(): Card {
        return Card (key, source.clone(), isUp, grade)
    }

    fun serialize (): Map<String, Any>
    {
        return mutableMapOf<String, Any>().apply {
            put("cardName", key)
            put("source", source.serializeF())
            put ("isUp", isUp)
            put ("grade", grade.name)
        }
    }
    override fun register() {
        GenshinDrawcard2.cardManager.registerCard(this)
    }

    override fun compareTo(other: CardStarGrade): Int {
        return if (this.grade.level > other.level)  1
        else if (this.grade.level == other.level)  0
        else  2
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Card

        return key == other.key
    }


    override fun hashCode(): Int {
        return key.hashCode()
    }



    override fun toString(): String {
        return "Card(key => '$key' \n isUp => '$isUp' \n source => '$source' \n grade => '${grade.content}')"
    }

    class Builder (
        val key: String,
        val source: ItemStack,
        val isUp: Boolean = false,
        val grade: CardStarGrade
    ){

        fun build (): Card
        {
            return Card (key, source, isUp, grade)
        }
    }

    companion object {
        @JvmStatic
        fun createCard(
            key: String,
            source: ItemStack = buildItem (XMaterial.BLACK_STAINED_GLASS)
            {
                name = "默认卡片"
                customModelData = 0
                lore.clear ()
                lore.addAll(listOf ("此卡片是默认的卡片"))
            },
            isUp: Boolean,
            grade: CardStarGrade = THREE_STAR
        ): Card {
            return Builder (key, source, isUp, grade).build()
        }

        @JvmStatic
        fun deserialize (item: Map<String, Any>): Card
        {
            val key = item["cardName"].toString()
            val source = (item["source"] as Map<String, Any>).deserializeF()
            val isUp = item["isUp"].cbool
            val grade = CardStarGrade.valueOf (item["grade"].toString())

            return createCard(key, source, isUp, grade)
        }
    }
}