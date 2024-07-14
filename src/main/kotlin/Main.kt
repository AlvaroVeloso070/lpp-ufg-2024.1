package com.webscrapper

import com.webscrapper.reit.persistence.ReitsQuery
import com.webscrapper.reit.persistence.ReitsQueryImpl
import com.webscrapper.scrappers.reit.ReitScrapper
import com.webscrapper.scrappers.stock.StockScrapper
import com.webscrapper.stock.persistence.StocksQuery
import com.webscrapper.stock.persistence.StocksQueryImpl
import java.util.*

val reitsQuery : ReitsQuery = ReitsQueryImpl()
val stocksQuery : StocksQuery = StocksQueryImpl()

fun main() {
    println("Preparando banco de dados...\n")
    StockScrapper().start()
    ReitScrapper().start()

    val scanner = Scanner(System.`in`)
    while (true) {
        println("\n========================")
        println("    MENU PRINCIPAL")
        println("========================")
        println("1 - Melhores ações do mercado (Análise Quantitativa)")
        println("2 - Consultar Ação por Ticker")
        println("3 - Melhores fundos imobiliários do mercado (Análise Quantitativa)")
        println("4 - Consultar Fundo Imobiliário por Ticker")
        println("0 - Sair")
        println("========================")
        print("Escolha uma opção: ")

        var opcao: Int? = null

        try{
            opcao = scanner.nextInt()
        }catch (_: InputMismatchException) {

        }

        when (opcao) {
            1 -> stocksQuery.melhoresAcoes()
            2 -> consultarAcaoPorTicker()
            3 -> reitsQuery.melhoresReits()
            4 -> consultarFundoImobiliarioPorTicker()
            0 -> {
                println("Saindo...")
                break
            }
            else -> println("Opção inválida, tente novamente.")
        }
    }
}

fun consultarFundoImobiliarioPorTicker() {
    val scanner = Scanner(System.`in`)
    print("Digite o ticker do fundo imobiliário: ")
    val ticker = scanner.next()
    reitsQuery.consultarReit(ticker)

}

fun consultarAcaoPorTicker() {
    val scanner = Scanner(System.`in`)
    print("Digite o ticker da ação: ")
    val ticker = scanner.next()
    stocksQuery.consultarAcao(ticker)
}