package com.webscrapper

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

object DBManager {

    private val dataSource: DataSource = createDataSource()

    init {
        Database.connect(dataSource)
    }

    private fun createDataSource(): DataSource {
        val dataSource = org.h2.jdbcx.JdbcDataSource()
        dataSource.setURL("jdbc:h2:mem:scrapper;DB_CLOSE_DELAY=-1;")
        dataSource.user = "root"
        dataSource.password = ""
        return dataSource
    }

    fun <T> inTransaction(block: () -> T): T {
        return transaction {
            block()
        }
    }
}