package cn.gionrose.facered.genshinDrawcard2.internal.feature.personal

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.CardDrawDetail
import cn.gionrose.facered.genshinDrawcard2.internal.feature.database.GenshinDrawcard2Container
import com.google.gson.GsonBuilder
import com.skillw.pouvoir.api.plugin.map.component.Keyable
import org.bukkit.entity.Player
import taboolib.common.util.unsafeLazy
import java.util.*

/**
 * @description 数据库操作 每个玩家的抽卡信息
 * @author facered
 * @date 2023/10/13 12:39
 */
class PersonalDrawCardDetailData (override val key: UUID, val detail: CardDrawDetail): Keyable<UUID> {

    companion object {
        private val gson by unsafeLazy {
            GsonBuilder ().create()
        }

        @JvmStatic
        fun deserialize(uuid: UUID, str: String): PersonalDrawCardDetailData? {

            return PersonalDrawCardDetailData(
                uuid,
                CardDrawDetail.deserialize(gson.fromJson<Map<String, Any>>(str, Map::class.java) ?: return null)
            )

        }

        @JvmStatic
        fun fromPlayer(player: Player): PersonalDrawCardDetailData {
            return PersonalDrawCardDetailData(player.uniqueId, GenshinDrawcard2.cardDrawDetailManager[player.uniqueId] ?: CardDrawDetail ())
        }

        @JvmStatic
        internal fun pushAttrData( player: Player) {
            val name = player.name
            GenshinDrawcard2Container[player.uniqueId.toString(), player.name] = fromPlayer(player).serialize()
        }

        @JvmStatic
        internal fun pullAttrData(player: Player): PersonalDrawCardDetailData? {
            val data = GenshinDrawcard2Container[player.uniqueId.toString(), player.name] ?: return null
            if (data == "null") return null
            return deserialize(player.uniqueId, data)
        }
    }

    fun serialize(): String {
        return gson.toJson(detail.serialize())
    }
}