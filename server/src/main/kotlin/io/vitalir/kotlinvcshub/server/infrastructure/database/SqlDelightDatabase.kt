package io.vitalir.kotlinvcshub.server.infrastructure.database

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig
import io.vitalir.kotlinvcshub.server.infrastructure.database.sqldelight.MainSqlDelight
import javax.sql.DataSource

private fun createDataSource(databaseConfig: AppConfig.Database): DataSource {
    val hikariConfig = HikariConfig().apply {
        dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
        username = databaseConfig.username
        password = databaseConfig.password
        addDataSourceProperty("databaseName", databaseConfig.databaseName)
        addDataSourceProperty("serverName", databaseConfig.serverName)
    }
    return HikariDataSource(hikariConfig)
}

fun createMainSqlDelightDatabase(databaseConfig: AppConfig.Database): MainSqlDelight {
    val sqlDriver = createDataSource(databaseConfig).asJdbcDriver()
    return MainSqlDelight(sqlDriver)
}
