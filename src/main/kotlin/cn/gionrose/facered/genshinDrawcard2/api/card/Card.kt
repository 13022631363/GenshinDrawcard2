package cn.gionrose.facered.genshinDrawcard2.api.card

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.CardStarGrade.THREE_STAR
import com.skillw.pouvoir.api.plugin.map.component.Registrable
import org.bukkit.inventory.ItemStack
import taboolib.common5.cbool
import taboolib.library.xseries.XMaterial
import taboolib.platform.util.buildItem
import taboolib.platform.util.deserializeToItemStack
import taboolib.platform.util.serializeToByteArray

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
            put("source", source.serializeToByteArray())
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
        fun deserialize (card: Map<String, Any>): Card
        {
            val key = card["cardName"].toString()
            val source = (card["source"] as ArrayList<Byte>).toByteArray().deserializeToItemStack()
            val isUp = card["isUp"].cbool
            val grade = CardStarGrade.valueOf (card["grade"].toString())

            return createCard(key, source, isUp, grade)
        }
    }
}