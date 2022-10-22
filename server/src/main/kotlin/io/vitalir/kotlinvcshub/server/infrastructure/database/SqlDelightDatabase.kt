package io.vitalir.kotlinvcshub.server.infrastructure.database

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.vitalir.kotlinvcshub.server.infrastructure.database.sqldelight.MainSqlDelight
import javax.sql.DataSource

private val dataSource: DataSource by lazy {
    val hikariConfig = HikariConfig().apply {
        dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
        username = "postgres" // TODO set from config
        password = "admin"
    }
    HikariDataSource(hikariConfig)
}

val mainSqlDelightDatabase: MainSqlDelight by lazy {
    MainSqlDelight(dataSource.asJdbcDriver())
}
