package com.webscrapper

interface Query {
    fun imprimirQuery(query: org.jetbrains.exposed.sql.Query)
}