package com.webscrapper.scrappers.reit

import com.webscrapper.reit.constants.ReitColumns.ALUGUEL_M2
import com.webscrapper.reit.constants.ReitColumns.CAP_RATE
import com.webscrapper.reit.constants.ReitColumns.COTACAO
import com.webscrapper.reit.constants.ReitColumns.DIVIDEND_YIELD
import com.webscrapper.reit.constants.ReitColumns.FFO_YIELD
import com.webscrapper.reit.constants.ReitColumns.LIQUIDEZ
import com.webscrapper.reit.constants.ReitColumns.NOME
import com.webscrapper.reit.constants.ReitColumns.PRECO_M2
import com.webscrapper.reit.constants.ReitColumns.PVP
import com.webscrapper.reit.constants.ReitColumns.QTD_IMOVEIS
import com.webscrapper.reit.constants.ReitColumns.SEGMENTO
import com.webscrapper.reit.constants.ReitColumns.VACANCIA_MEDIA
import com.webscrapper.reit.constants.ReitColumns.VALOR_DE_MERCADO
import com.webscrapper.reit.persistence.Reits
import com.webscrapper.scrappers.Scrapper
import com.webscrapper.stock.constants.StockColumns
import org.jetbrains.exposed.sql.insert
import java.io.IOException

class ReitScrapper : Scrapper<Reits>("https://www.fundamentus.com.br/fii_resultado.php", Reits) {

    @Throws(IOException::class)
    override fun scrap() {
        getTableRows().forEach { row ->
            try {
                Reits.insert { reit ->
                    reit[papel] = getTickerPapelByIndex(row, NOME)
                    reit[nome] = getElementTitleByIndex(row, NOME)
                    reit[segmento] = getElementValueByIndex(row, SEGMENTO)
                    reit[cotacao] = getElementDoubleValueByIndex(row, COTACAO)
                    reit[ffoYield] = getElementDoubleValueByIndex(row, FFO_YIELD)
                    reit[dividendYield] = getElementDoubleValueByIndex(row, DIVIDEND_YIELD)
                    reit[pvp] = getElementDoubleValueByIndex(row, PVP)
                    reit[valorDeMercado] = getElementDoubleValueByIndex(row, VALOR_DE_MERCADO)
                    reit[liquidez] = getElementDoubleValueByIndex(row, LIQUIDEZ)
                    reit[quantidadeImoveis] = getElementValueByIndex(row, QTD_IMOVEIS).toInt()
                    reit[precoPorM2] = getElementDoubleValueByIndex(row, PRECO_M2)
                    reit[aluguelPorM2] = getElementDoubleValueByIndex(row, ALUGUEL_M2)
                    reit[capRate] = getElementDoubleValueByIndex(row, CAP_RATE)
                    reit[vacanciaMedia] = getElementDoubleValueByIndex(row, VACANCIA_MEDIA)
                }
            } catch (e: Exception) {
                println("Erro ao inserir o papel ${getTickerPapelByIndex(row, StockColumns.NOME)}")
            }
        }
    }
}
