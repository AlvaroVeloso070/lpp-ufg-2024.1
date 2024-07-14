package com.webscrapper.scrappers

import com.webscrapper.DBManager
import com.webscrapper.util.Utils
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException
import java.util.*

abstract class Scrapper<T : Table> protected constructor(private var url: String, private val tableClass: T) {

    private val urlConsultaAtivo = "https://www.fundamentus.com.br/detalhes.php?papel="

    fun start(){
        try {
            DBManager.inTransaction {
                SchemaUtils.create(tableClass)
                scrap()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    abstract fun scrap()

    @Throws(IOException::class)
    protected fun getDocument(): Document {
        val response = Jsoup.connect(url)
            .method(Connection.Method.GET)
            .execute()

        return Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
            .referrer("https://www.google.com")
            .cookies(response.cookies())
            .get()
    }

    @Throws(IOException::class)
    fun consultarAtivo(ativo: String): Document {
        return Jsoup.connect(urlConsultaAtivo + ativo).get()
    }

    @Throws(IOException::class)
    protected fun getNomeAtivos(): MutableList<String> {
        val nomeAtivos = mutableListOf<String>()
        getDocument().getElementsByClass("tips").forEach { element ->
            nomeAtivos.add(element.text())
        }

        return nomeAtivos
    }

    @Throws(IOException::class)
    protected fun getTableRows(): Elements {
        return getDocument().getElementsByTag("tbody").select("tr")
    }

    protected fun getTickerPapelByIndex(element: Element, index: Int): String {
        return element.getElementsByIndexEquals(index).select("td > span > a").text()
    }

    protected fun getElementValueByIndex(element: Element, index: Int): String {
        return element.getElementsByTag("td")[index].text()
    }

    protected fun getElementDoubleValueByIndex(element: Element, index: Int): Double {
        var value = Utils.handleDecimalSeparators(getElementValueByIndex(element, index))

        if (value.contains("%")) {
            value = value.replace("%", "")
        }

        return value.toDouble()
    }

    protected fun getElementTitleByIndex(element: Element, index: Int): String {
        return element.getElementsByIndexEquals(index).attr("title")
    }
}

