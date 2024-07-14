package com.webscrapper.stock.persistence

import com.webscrapper.DBManager
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import java.text.DecimalFormat
import java.util.*

interface StocksQuery : com.webscrapper.Query {
    fun melhoresAcoes()
    fun consultarAcao(papel: String)
}

class StocksQueryImpl : StocksQuery {
    override fun melhoresAcoes() {
        println()
        DBManager.inTransaction {
            val query = Stocks.selectAll()
                .where { Stocks.dividendYield greater 6.0 }
                .andWhere { Stocks.precoSobreLucro greater 0.0 }
                .andWhere { Stocks.pvp greater 0.7 }
                .andWhere { Stocks.roe greater 15.0 }
                .andWhere { Stocks.patrimonioLiquido greater 0.0 }
                .orderBy(Stocks.liquidez2meses, SortOrder.DESC)
                .limit(20)

            imprimirQuery(query)
        }
    }

    override fun consultarAcao(papel: String) {
        println()
        DBManager.inTransaction {
            val query = Stocks.selectAll()
                .where { Stocks.papel like "%${papel.uppercase(Locale.getDefault())}%" }

            if (query.empty()) {
                println("Ação não encontrada")
            } else {
                imprimirQuery(query)
            }
        }
    }

    override fun imprimirQuery(query: Query) {
        val df = DecimalFormat("#,##0.00")
        val papel_length = 8
        val cotacao_length = 13
        val pvp_length = 5
        val dividendYield_length = 19
        val roe_length = 8
        val pl_length = 6
        val patrimonioLiquido_length = 24
        val liquidez_length = 16
        val header =
            "| Papel   | Cotação (R$) | Dividend Yield (%) | ROE (%) | P/L   | P/VP | Patrimônio Líquido (R$) | Liquidez mensal |"
        val separator =
            "+---------+--------------+--------------------+---------+-------+------+-------------------------+-----------------+"

        println(separator)
        println(header)
        println(separator)

        query.forEach { row ->
            val papel = row[Stocks.papel]
            val cotacao = df.format(row[Stocks.cotacao])
            val dividendYield = df.format(row[Stocks.dividendYield])
            val roe = df.format(row[Stocks.roe])
            val pl = df.format(row[Stocks.precoSobreLucro])
            val pvp = df.format(row[Stocks.pvp])
            val patrimonioLiquido = df.format(row[Stocks.patrimonioLiquido])
            val liquidezMensal = df.format(row[Stocks.liquidez2meses] / 2)

            println("| $papel${getSpacesForColumn(papel_length, papel)}| $cotacao${getSpacesForColumn(cotacao_length, cotacao)}| $dividendYield${getSpacesForColumn(dividendYield_length, dividendYield)}|" +
                    " $roe${getSpacesForColumn(roe_length, roe)}| $pl${getSpacesForColumn(pl_length, pl)}| $pvp${getSpacesForColumn(pvp_length, pvp)}| " +
                    "$patrimonioLiquido${getSpacesForColumn(patrimonioLiquido_length, patrimonioLiquido)}| $liquidezMensal${getSpacesForColumn(liquidez_length, liquidezMensal)}|")
        }

        println(separator)
    }

    private fun getSpacesForColumn(columnLength : Int, value : String): String {
        val spaces = (columnLength - value.length)
        return " ".repeat(spaces)
    }
}