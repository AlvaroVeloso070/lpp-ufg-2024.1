package com.webscrapper.reit.persistence

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Reits : Table() {
    val id = long("id").autoIncrement()
    val papel : Column<String> = varchar("papel", 8)
    val nome = varchar("nome", 255)
    val segmento = varchar("segmento", 255)
    val cotacao = double("cotacao")
    val ffoYield = double("ffoYield")
    val dividendYield = double("dividendYield")
    val pvp = double("pvp")
    val valorDeMercado = double("valorDeMercado")
    val liquidez = double("liquidez")
    val quantidadeImoveis = integer("quantidadeImoveis")
    val precoPorM2 = double("precoPorM2")
    val aluguelPorM2 = double("aluguelPorM2")
    val capRate = double("capRate")
    val vacanciaMedia = double("vacanciaMedia")

    override val primaryKey = PrimaryKey(papel, name = "PK_Reit")
}
