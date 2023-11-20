package cn.gionrose.facered.genshinDrawcard2.internal.feature.database

import cn.gionrose.facered.genshinDrawcard2.api.card.Card
import cn.gionrose.facered.genshinDrawcard2.api.card.CardDrawDetail
import com.google.gson.Gson
import org.bukkit.entity.Player
import taboolib.common5.cbool
import taboolib.common5.cint

object GenshinDrawCard2Database{

    val gson = Gson ()
    fun createTable ()
    {
        try {
            MysqlWrapper.connection().use {
                it.prepareStatement("create table if not exists genshindrawcard_count (\n" +
                        "        uuid varchar (100),\n" +
                        "        username varchar(64),\n" +
                        "        pool_name varchar(64),\n" +
                        "        three_draw_count int,\n" +
                        "        four_draw_count int,\n" +
                        "        five_draw_count int,\n" +
                        "        three_small_guarantee_count int,\n" +
                        "        four_small_guarantee_count int,\n" +
                        "        five_small_guarantee_count int,\n" +
                        "        three_big_guarantee boolean,\n" +
                        "        four_big_guarantee boolean,\n" +
                        "        five_big_guarantee boolean,\n" +
                        "        total_count int,\n" +
                        "        unique key (uuid, username, pool_name),\n" +
                        "        unique key (uuid),\n" +
                        "        unique key (uuid, username)\n" +
                        "          ) ;").execute()
                it.prepareStatement("create table if not exists genshindrawcard_draw_record (\n" +
                        "        uuid varchar(100),\n" +
                        "        username varchar(64),\n" +
                        "        `date` datetime,\n" +
                        "        record text \n" +
                        ");").execute()
            }
        }catch (e: ExceptionInInitializerError)
        {
            throw RuntimeException ("请配置好数据库信息 ...")
        }
    }

    fun deleteTable (tableName: String): Boolean
    {
        try {
            MysqlWrapper.connection().use {
                return it.prepareStatement("drop table  if  exists $tableName;").execute()
            }
        }catch (e: ExceptionInInitializerError)
        {
            throw RuntimeException ("请配置好数据库信息 ...")
        }
    }
    fun initPlayerDrawCount (player: Player, poolName: String)
    {
        try {
            MysqlWrapper.connection().use {connext ->
                connext.prepareStatement("insert into genshindrawcard_count values (?,?,?,?,?,?,?,?,?,?,?,?,?);").apply {
                    setString(1, player.uniqueId.toString())
                    setString(2, player.name)
                    setString (3, poolName)
                    setInt(4, 0 )
                    setInt(5, 0 )
                    setInt(6, 0 )
                    setInt(7, 0 )
                    setInt(8, 0 )
                    setInt(9, 0 )
                    setBoolean(10, false )
                    setBoolean(11, false )
                    setBoolean(12, false )
                    setInt(13, 0)
                }.execute ()
            }
        }catch (e: ExceptionInInitializerError)
        {
            throw RuntimeException ("请配置好数据库信息 ...")
        }
    }

    fun updatePlayerDrawCount (player: Player, detail: CardDrawDetail)
    {
        try {
            MysqlWrapper.connection().use {connext ->
                connext.prepareStatement("update genshindrawcard_count\n" +
                        "    set three_draw_count = ?,\n" +
                        "        four_draw_count = ?,\n" +
                        "        five_draw_count = ?,\n" +
                        "        three_small_guarantee_count = ?,\n" +
                        "        four_small_guarantee_count = ?,\n" +
                        "        five_small_guarantee_count = ?,\n" +
                        "        three_big_guarantee = ?,\n" +
                        "        four_big_guarantee = ?,\n" +
                        "        five_big_guarantee = ?,\n" +
                        "        total_count = ? where uuid = ? AND username = ? AND pool_name = ?;").apply {

                    setInt(1, detail["三星抽卡总次数"].cint )
                    setInt(2, detail["四星抽卡总次数"].cint )
                    setInt(3, detail["五星抽卡总次数"].cint )
                    setInt(4, detail["三星小保底次数"].cint )
                    setInt(5, detail["四星小保底次数"].cint )
                    setInt(6, detail["五星小保底次数"].cint)
                    setBoolean(7, detail["三星大保底触发"].cbool )
                    setBoolean(8, detail["四星大保底触发"].cbool )
                    setBoolean(9, detail["五星大保底触发"].cbool )
                    setInt(10, detail["抽卡总次数"].cint)
                    setString(11, player.uniqueId.toString())
                    setString(12, player.name)
                    setString(13, detail.key)
                }.execute ()
            }
        }catch (e: ExceptionInInitializerError)
        {
            throw RuntimeException ("请配置好数据库信息 ...")
        }
    }

    fun selectPlayerDrawCount (player: Player, poolName: String): CardDrawDetail?
    {
        var result :CardDrawDetail? = null
        try {
            MysqlWrapper.connection().use {connext ->
                connext.prepareStatement("select * from genshindrawcard_count where uuid = ? AND username = ? AND pool_name = ?;").apply {
                    setString(1, player.uniqueId.toString())
                    setString(2, player.name)
                    setString(3, poolName)

                }.executeQuery ().apply {
                   if (next())
                   {
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
                }
            }
        }catch (e: ExceptionInInitializerError)
        {
            throw RuntimeException ("请配置好数据库信息 ...")
        }

        return result
    }

    fun insertRecord (player: Player, record: Card, date: String)
    {
        try {
            MysqlWrapper.connection().use {connext ->
                connext.prepareStatement("insert into genshindrawcard_draw_record values (?,?,?,?);").apply {
                    setString(1, player.uniqueId.toString())
                    setString(2, player.name)
                    setString(3, date)
                    setString(4, Gson ().toJson(record.serialize()))
                }.execute ()
            }
        }catch (e: ExceptionInInitializerError)
        {
            throw RuntimeException ("请配置好数据库信息 ...")
        }
    }


    fun selectRecord (player: Player): List<Card>
    {
        val result = mutableListOf<Card>()
        try {
            MysqlWrapper.connection().use {connext ->
                connext.prepareStatement("select record from genshindrawcard_draw_record where uuid = ? AND username = ? order by `date` desc;").apply {
                    setString(1, player.uniqueId.toString())
                    setString(2, player.name)
                }.executeQuery().apply {
                    while (next())
                    {
                        result.add(Card.deserialize(Gson ().fromJson<Map<String, Any>>(getString("record"), Map::class.java)))
                    }
                }
            }
        }catch (e: ExceptionInInitializerError)
        {
            throw RuntimeException ("请配置好数据库信息 ...")
        }
        return result
    }


    fun ifCount100DeleteOldRecord (player: Player)
    {
        try {
            MysqlWrapper.connection().use {connext ->

                var recordCount: Int = 0

                connext.prepareStatement("select count(*) from genshindrawcard_draw_record where uuid = ? AND username = ?;").apply {
                    setString(1, player.uniqueId.toString())
                    setString (2, player.name)
                }.executeQuery().apply {
                    if (next())
                        recordCount = getInt("count(*)")
                }
                if (recordCount >= 100)
                {
                    val row = connext.prepareStatement("delete from genshindrawcard_draw_record where date in (SELECT r1.date FROM (SELECT * FROM genshindrawcard_draw_record where username = ? AND uuid = ? ORDER BY `date`)  AS r1) LIMIT ?;").apply {
                        setString(1, player.name)
                        setString(2, player.uniqueId.toString())
                        setInt (3, recordCount - 100)
                    }.executeUpdate()
                }

            }
        }catch (e: ExceptionInInitializerError)
        {
            throw RuntimeException ("请配置好数据库信息 ...")
        }
    }

    fun deleteCountByPoolName (poolName: String)
    {

        try {
            MysqlWrapper.connection().use {connext ->
                connext.prepareStatement("delete from genshindrawcard_count where pool_name = ?;").apply {
                    setString(1, poolName)
                }.execute()
            }
        }catch (e: ExceptionInInitializerError)
        {
            throw RuntimeException ("请配置好数据库信息 ...")
        }
    }


}