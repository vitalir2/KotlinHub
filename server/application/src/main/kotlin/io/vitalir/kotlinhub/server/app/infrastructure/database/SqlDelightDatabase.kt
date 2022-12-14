package io.vitalir.kotlinhub.server.app.infrastructure.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.database.sqldelight.MainSqlDelight
import javax.sql.DataSource

private var cachedDatabase: MainSqlDelight? = null
private val lock = Any()

fun createMainSqlDelightDatabase(databaseConfig: AppConfig.Database): MainSqlDelight = synchronized(lock) {
    cachedDatabase?.let { db -> return db }
    val sqlDriver = createDataSource(databaseConfig).asJdbcDriver()
    MainSqlDelight.Schema.createOrIgnore(sqlDriver)
    return MainSqlDelight(sqlDriver).also { cachedDatabase = it }
}

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

private fun SqlSchema.createOrIgnore(driver: SqlDriver) {
    try {
        create(driver)
    } catch (exception: Exception) {
        // Ignore
    }
}
