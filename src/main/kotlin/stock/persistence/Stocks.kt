package com.webscrapper.stock.persistence

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Stocks : Table() {
    val id = long("id").autoIncrement()
    val papel : Column<String> = varchar("papel", 8)
    val nome = varchar("nome", 255)
    val cotacao = double("cotacao")
    val precoSobreLucro = double("precoSobreLucro")
    val pvp = double("pvp")
    val psr = double("psr")
    val dividendYield = double("dividendYield")
    val precoSobreAtivo = double("precoSobreAtivo")
    val precoSobreCapGiro = double("precoSobreCapGiro")
    val precoSobreEbitda = double("precoSobreEbitda")
    val precoSobreAtivoCircLiquido = double("precoSobreAtivoCircLiquido")
    val evSobreEbit = double("evSobreEbit")
    val evSobreEbitda = double("evSobreEbitda")
    val margemEbitda = double("margemEbitda")
    val margemLiquida = double("margemLiquida")
    val liquidezCorrente = double("liquidezCorrente")
    val roic = double("roic")
    val roe = double("roe")
    val liquidez2meses = double("liquidez2meses")
    val patrimonioLiquido = double("patrimonioLiquido")
    val dividaBrutaSobrePatrimonio = double("dividaBrutaSobrePatrimonio")
    val crescimentoReceitaLiquida5anos = double("crescimentoReceitaLiquida5anos")

    override val primaryKey = PrimaryKey(papel, name = "PK_Stock")
}

