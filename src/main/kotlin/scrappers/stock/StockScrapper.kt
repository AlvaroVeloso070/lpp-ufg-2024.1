package com.webscrapper.scrappers.stock

import com.webscrapper.scrappers.Scrapper
import com.webscrapper.stock.constants.StockColumns.COTACAO
import com.webscrapper.stock.constants.StockColumns.CRESCIMENTO_RECEITA_5_ANOS
import com.webscrapper.stock.constants.StockColumns.DIVIDA_SOBRE_PATRIMONIO
import com.webscrapper.stock.constants.StockColumns.DY
import com.webscrapper.stock.constants.StockColumns.EVSEBIT
import com.webscrapper.stock.constants.StockColumns.EVSEBITDA
import com.webscrapper.stock.constants.StockColumns.LIQUIDEZ_2_MESES
import com.webscrapper.stock.constants.StockColumns.LIQUIDEZ_CORRENTE
import com.webscrapper.stock.constants.StockColumns.MARGEM_EBTIDA
import com.webscrapper.stock.constants.StockColumns.MARGEM_LIQUIDA
import com.webscrapper.stock.constants.StockColumns.NOME
import com.webscrapper.stock.constants.StockColumns.PATRIMONIO_LIQUIDO
import com.webscrapper.stock.constants.StockColumns.PL
import com.webscrapper.stock.constants.StockColumns.PSA
import com.webscrapper.stock.constants.StockColumns.PSACL
import com.webscrapper.stock.constants.StockColumns.PSCG
import com.webscrapper.stock.constants.StockColumns.PSEBTIDA
import com.webscrapper.stock.constants.StockColumns.PSR
import com.webscrapper.stock.constants.StockColumns.PVP
import com.webscrapper.stock.constants.StockColumns.ROE
import com.webscrapper.stock.constants.StockColumns.ROIC
import com.webscrapper.stock.persistence.Stocks
import org.jetbrains.exposed.sql.*
import java.io.IOException

class StockScrapper : Scrapper<Stocks>("https://www.fundamentus.com.br/resultado.php", Stocks) {

    @Throws(IOException::class)
    override fun scrap() {
        getTableRows().forEach { row ->
            try{
                Stocks.insert { stock ->
                    stock[papel] = getTickerPapelByIndex(row, NOME)
                    stock[nome] = getElementTitleByIndex(row, NOME)
                    stock[cotacao] = getElementDoubleValueByIndex(row, COTACAO)
                    stock[precoSobreLucro] = getElementDoubleValueByIndex(row, PL)
                    stock[pvp] = getElementDoubleValueByIndex(row, PVP)
                    stock[psr] = getElementDoubleValueByIndex(row, PSR)
                    stock[dividendYield] = getElementDoubleValueByIndex(row, DY)
                    stock[precoSobreAtivo] = getElementDoubleValueByIndex(row, PSA)
                    stock[precoSobreCapGiro] = getElementDoubleValueByIndex(row, PSCG)
                    stock[precoSobreEbitda] = getElementDoubleValueByIndex(row, PSEBTIDA)
                    stock[precoSobreAtivoCircLiquido] = getElementDoubleValueByIndex(row, PSACL)
                    stock[evSobreEbit] = getElementDoubleValueByIndex(row, EVSEBIT)
                    stock[evSobreEbitda] = getElementDoubleValueByIndex(row, EVSEBITDA)
                    stock[margemEbitda] = getElementDoubleValueByIndex(row, MARGEM_EBTIDA)
                    stock[margemLiquida] = getElementDoubleValueByIndex(row, MARGEM_LIQUIDA)
                    stock[liquidezCorrente] = getElementDoubleValueByIndex(row, LIQUIDEZ_CORRENTE)
                    stock[roic] = getElementDoubleValueByIndex(row, ROIC)
                    stock[roe] = getElementDoubleValueByIndex(row, ROE)
                    stock[liquidez2meses] = getElementDoubleValueByIndex(row, LIQUIDEZ_2_MESES)
                    stock[patrimonioLiquido] = getElementDoubleValueByIndex(row, PATRIMONIO_LIQUIDO)
                    stock[dividaBrutaSobrePatrimonio] = getElementDoubleValueByIndex(row, DIVIDA_SOBRE_PATRIMONIO)
                    stock[crescimentoReceitaLiquida5anos] =
                        getElementDoubleValueByIndex(row, CRESCIMENTO_RECEITA_5_ANOS)
                }
            }catch (e: Exception) {
                println("Erro ao inserir o papel ${getTickerPapelByIndex(row, NOME)}")
            }
        }
    }
}
