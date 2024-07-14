package com.webscrapper.reit.persistence

import com.webscrapper.DBManager
import com.webscrapper.Query
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import java.text.DecimalFormat
import java.util.*

interface ReitsQuery : Query {
    fun melhoresReits()
    fun consultarReit(papel: String)
}

class ReitsQueryImpl : ReitsQuery {
    override fun melhoresReits() {
        DBManager.inTransaction {
            val query = Reits.selectAll()
                .where { Reits.dividendYield greaterEq 8.0 }
                .andWhere { Reits.valorDeMercado greater 100000000.0 }
                .andWhere { Reits.liquidez greater 1000000.0 }
                .andWhere { Reits.pvp lessEq 1.2 }
                .andWhere { Reits.pvp greaterEq 0.8 }
                .andWhere {
                    (Reits.quantidadeImoveis eq 0) or (Reits.vacanciaMedia less 15.0)
                }
                .orderBy(Reits.valorDeMercado, SortOrder.DESC)
                .limit(20)

            imprimirQuery(query)
        }
    }

    override fun consultarReit(papel: String) {
        println()
        DBManager.inTransaction {
            val query = Reits.selectAll()
                .where { Reits.papel like "%${papel.uppercase(Locale.getDefault())}%" }

            if (query.empty()) {
                println("FII não encontrado")
            } else {
                imprimirQuery(query)
            }
        }
    }

    override fun imprimirQuery(query: org.jetbrains.exposed.sql.Query) {
        val df = DecimalFormat("#,##0.00")
        val cotacao_length = 13
        val pvp_length = 5
        val dividendYield_length = 19
        val segmento_length = 20
        val qtdImoveis_length = 13
        val vacanciaMedia_length = 19
        val liquidez_length = 14
        val valorDeMercado_length = 22


        val header =
            "| Papel  | Cotação (R$) | PVP  | Dividend Yield (%) | Segmento            | Qtd. Imoveis | Vacancia Média (%) | Liquidez (R$) | Valor de Mercado (R$) |"
        val separator =
            "+--------+--------------+------+--------------------+---------------------+--------------+--------------------+---------------+-----------------------+"

        println(separator)
        println(header)
        println(separator)

        query.forEach { row ->
            val papel = row[Reits.papel]
            val cotacao = df.format(row[Reits.cotacao])
            val dividendYield = df.format(row[Reits.dividendYield])
            val pvp = df.format(row[Reits.pvp])
            val valorDeMercado = df.format(row[Reits.valorDeMercado])
            val liquidez = df.format(row[Reits.liquidez])
            val segmento = row[Reits.segmento]
            val qtdImoveis = row[Reits.quantidadeImoveis]
            val vacanciaMedia = df.format(row[Reits.vacanciaMedia])

            println(
                "| $papel | $cotacao${getSpacesForColumn(cotacao_length, cotacao)}| $pvp${getSpacesForColumn(pvp_length, pvp)}| $dividendYield${getSpacesForColumn(dividendYield_length, dividendYield)}| $segmento${getSpacesForColumn(segmento_length, segmento)}| $qtdImoveis${getSpacesForColumn(qtdImoveis_length,
                    qtdImoveis.toString())}| $vacanciaMedia${getSpacesForColumn(vacanciaMedia_length, vacanciaMedia)}| $liquidez${getSpacesForColumn(liquidez_length, liquidez)}| $valorDeMercado${getSpacesForColumn(valorDeMercado_length, valorDeMercado)}|"
            )
        }

        println(separator)
    }

    private fun getSpacesForColumn(columnLength : Int, value : String): String {
        val spaces = (columnLength - value.length)
        return " ".repeat(spaces)
    }

}