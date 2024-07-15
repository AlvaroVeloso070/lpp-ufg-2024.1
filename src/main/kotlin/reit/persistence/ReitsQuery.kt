package com.webscrapper.reit.persistence

import com.webscrapper.DBManager
import com.webscrapper.Query
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import java.text.DecimalFormat
import java.util.*

interface ReitsQuery : Query {
    fun melhoresReitsPapel()
    fun melhoresReitsTijolo()
    fun consultarReit(papel: String)
}

class ReitsQueryImpl : ReitsQuery {

    override fun melhoresReitsPapel() {
        DBManager.inTransaction {
            val query = Reits.selectAll()
                .where { Reits.dividendYield greaterEq 8.0 }
                .andWhere { Reits.valorDeMercado greater 100000000.0 }
                .andWhere { Reits.liquidez greater 1500000.0 }
                .andWhere { Reits.pvp lessEq 1.02 }
                .andWhere { Reits.pvp greaterEq 0.9 }
                .andWhere { Reits.quantidadeImoveis eq 0 }
                .orderBy(Reits.liquidez, SortOrder.DESC)
                .limit(10)

            imprimirQuery(query)
        }
    }

    override fun melhoresReitsTijolo() {
        DBManager.inTransaction {
            val query = Reits.selectAll()
                .where { Reits.dividendYield greaterEq 8.0 }
                .andWhere { Reits.valorDeMercado greater 100000000.0 }
                .andWhere { Reits.liquidez greater 1500000.0 }
                .andWhere { Reits.pvp lessEq 1.02 }
                .andWhere { Reits.pvp greaterEq 0.9 }
                .andWhere { Reits.quantidadeImoveis greater 0 }
                .andWhere { Reits.vacanciaMedia less 15.0 }
                .orderBy(Reits.liquidez, SortOrder.DESC)
                .limit(10)

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
        val cotacaoLength = 13
        val pvpLength = 5
        val dividendyieldLength = 19
        val segmentoLength = 20
        val qtdimoveisLength = 13
        val vacanciamediaLength = 19
        val liquidezLength = 14
        val valordemercadoLength = 22


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
                "| $papel | $cotacao${getSpacesForColumn(cotacaoLength, cotacao)}| $pvp${getSpacesForColumn(pvpLength, pvp)}| $dividendYield${getSpacesForColumn(dividendyieldLength, dividendYield)}| $segmento${getSpacesForColumn(segmentoLength, segmento)}| $qtdImoveis${getSpacesForColumn(qtdimoveisLength,
                    qtdImoveis.toString())}| $vacanciaMedia${getSpacesForColumn(vacanciamediaLength, vacanciaMedia)}| $liquidez${getSpacesForColumn(liquidezLength, liquidez)}| $valorDeMercado${getSpacesForColumn(valordemercadoLength, valorDeMercado)}|"
            )
        }

        println(separator)
    }

    private fun getSpacesForColumn(columnLength : Int, value : String): String {
        val spaces = (columnLength - value.length)
        return " ".repeat(spaces)
    }

}