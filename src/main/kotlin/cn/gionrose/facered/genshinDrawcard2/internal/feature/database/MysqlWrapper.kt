package cn.gionrose.facered.genshinDrawcard2.internal.feature.database

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardDrawDetail
import com.google.gson.Gson
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.bukkit.entity.Player
import taboolib.common.env.RuntimeDependency
import taboolib.common5.cbool
import taboolib.common5.cint
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.database.*

import java.sql.Connection

@RuntimeDependency ("!com.zaxxer:HikariCP:4.0.3")
object MysqlWrapper
{
    val host = HostSQL (GenshinDrawcard2.configManager.databaseHost!!)
    val dataSource by lazy { host.createDataSource() }


    val drawCardCountTable = Table ("genshindrawcard_count", host){
        add ("uuid") {
            type(ColumnTypeSQL.VARCHAR, 100){
                options(ColumnOptionSQL.UNIQUE_KEY)
            }

        }
        add ("username") {
            type(ColumnTypeSQL.VARCHAR, 64){
                options(ColumnOptionSQL.UNIQUE_KEY)
            }
        }
        add ("pool_name") {
            type(ColumnTypeSQL.VARCHAR, 64){
                options(ColumnOptionSQL.UNIQUE_KEY)
            }
        }
        add ("three_draw_count") {
            type(ColumnTypeSQL.INT)
        }
        add ("four_draw_count") {
            type(ColumnTypeSQL.INT)
        }
        add ("five_draw_count") {
            type(ColumnTypeSQL.INT)
        }
        add ("three_small_guarantee_count") {
            type(ColumnTypeSQL.INT)
        }
        add ("four_small_guarantee_count") {
            type(ColumnTypeSQL.INT)
        }
        add ("five_small_guarantee_count") {
            type(ColumnTypeSQL.INT)
        }
        add ("three_big_guarantee") {
            type(ColumnTypeSQL.BOOLEAN)
        }
        add ("four_big_guarantee") {
            type(ColumnTypeSQL.BOOLEAN)
        }
        add ("five_big_guarantee") {
            type(ColumnTypeSQL.BOOLEAN)
        }
        add ("total_count") {
            type(ColumnTypeSQL.INT)
        }

    }

    val drawCardRecord = Table ("genshindrawcard_draw_record", host){
        add ("uuid") {
            type(ColumnTypeSQL.VARCHAR, 100)
        }
        add ("username") {
            type(ColumnTypeSQL.VARCHAR, 100)
        }
        add ("date") {
            type(ColumnTypeSQL.DATETIME)
        }
        add ("record") {
            type(ColumnTypeSQL.TEXT, 100)
        }
    }



    fun deleteTable (table: Table<HostSQL, *>)
    {
        table.delete(dataSource) {}
    }

    fun initPlayerDrawCount (player: Player, poolName: String)
    {

        drawCardRecord.transaction(dataSource){

        }
        drawCardCountTable.insert(dataSource, "uuid",
            "username", "pool_name", "three_draw_count",
            "four_draw_count", "five_draw_count", "three_small_guarantee_count",
            "four_small_guarantee_count", "five_small_guarantee_count",
            "three_big_guarantee", "four_big_guarantee", "five_big_guarantee",
            "total_count"){
            value(player.uniqueId.toString(),
                player.name,
                poolName,
                0, 0, 0, 0, 0, 0,
                false, false, false,
                0)
        }
    }

    fun updatePlayerDrawCount (player: Player, detail: CardDrawDetail)
    {
        drawCardCountTable.update(dataSource){
            set("three_draw_count", detail["三星抽卡总次数"].cint)
            set("four_draw_count", detail["四星抽卡总次数"].cint)
            set("five_draw_count", detail["五星抽卡总次数"].cint)
            set("three_small_guarantee_count", detail["三星小保底次数"].cint)
            set("four_small_guarantee_count", detail["四星小保底次数"].cint)
            set("five_small_guarantee_count", detail["五星小保底次数"].cint)
            set("three_big_guarantee", detail["三星大保底触发"].cbool)
            set("four_big_guarantee", detail["四星大保底触发"].cbool)
            set("five_big_guarantee", detail["五星大保底触发"].cbool)
            set ("total_count", detail["抽卡总次数"].cint)
            where ("uuid" eq player.uniqueId.toString() and("username" eq player.uniqueId.toString()) and ("pool_name" eq detail.key))
        }

    }
    fun selectPlayerDrawCount (player: Player, poolName: String): CardDrawDetail?
    {
        var result :CardDrawDetail? = null

        drawCardCountTable.select(dataSource){

            rows(*drawCardCountTable.columns.toTypedArray().map {
                (it as ColumnSQL).name
            }.toTypedArray())
            where("uuid" eq player.uniqueId.toString() and ("username" eq  player.name) and ("pool_name" eq poolName))
        }.forEach {
            result = CardDrawDetail(getString("pool_name"))
            result?.let {
                it["三星抽卡总次数"] = getInt("three_draw_count")
                it["四星抽卡总次数"] = getInt("four_draw_count")
                it["五星抽卡总次数"] = getInt("five_draw_count")
                it["三星小保底次数"] = getInt("three_small_guarantee_count")
                it["四星小保底次数"] = getInt("four_small_guarantee_count")
                it["五星小保底次数"] = getInt("five_small_guarantee_count")
                it["三星大保底触发"] = getBoolean("three_big_guarantee")
                it["四星大保底触发"] = getBoolean("four_big_guarantee")
                it["五星大保底触发"] = getBoolean("five_big_guarantee")
                it["抽卡总次数"] = getInt("total_count")
            }
        }
        return result
    }

    fun insertRecord (player: Player, record: Card, date: String){
        drawCardRecord.insert(dataSource, "uuid", "username", "date", "record"){
            value(player.uniqueId.toString(), player.name, date, Gson().toJson(record.serialize()))
        }
    }

    fun selectRecord (player: Player): List<Card>{
        val result = mutableListOf<Card>()
        drawCardRecord.select(dataSource){
            rows("record")
            where("uuid" eq player.uniqueId.toString() and ("username" eq player.name))
            orderBy("date", Order.Type.DESC)
        }.forEach {
            result.add(Card.deserialize(Gson ().fromJson<Map<String, Any>>(getString("record"), Map::class.java)))
        }
        return result
    }

    fun ifCount100DeleteOldRecord (player: Player){

        var recordCount: Int = 0
        drawCardRecord.select(dataSource){
            rows("count(*)")
            where("uuid" eq  player.uniqueId.toString() and ("username" eq player.name))
        }.forEach {
            recordCount = getInt("count(*)")
        }

        if (recordCount >= 100)
        {
            val dates = drawCardRecord.select(dataSource){
                rows("date")
                where("username" eq player.name and ("uuid" eq player.uniqueId.toString()) )
                orderBy("date")
                limit(recordCount - 100)
            }.map { getString("date") }

            drawCardRecord.delete(dataSource){
                where ("date" inside dates.toTypedArray())
            }
        }
    }

    fun deleteCountByPoolName (poolName: String){
        drawCardCountTable.delete(dataSource){
            where("pool_name" eq poolName)
        }
    }

}