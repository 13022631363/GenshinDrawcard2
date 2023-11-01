package cn.gionrose.facered.genshinDrawcard2.internal.feature.database

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import taboolib.common.env.RuntimeDependency

import java.sql.Connection

@RuntimeDependency ("!com.zaxxer:HikariCP:4.0.3")
object MysqlWrapper
{

    var dataSource: HikariDataSource


    init {

        val config = HikariConfig ().apply {
            jdbcUrl = GenshinDrawcard2.configManager.databaseConfig["url"].toString()
            username = GenshinDrawcard2.configManager.databaseConfig["username"].toString()
            password = GenshinDrawcard2.configManager.databaseConfig["password"].toString()
            maxLifetime = 1200000
        }
        try {
            dataSource = HikariDataSource(config)
        }catch (e: Exception)
        {
            throw  RuntimeException ("请修改 config.yml 中的 database 信息 ...")
        }

    }

    fun connection (): Connection
    {
        return dataSource.connection
    }

}