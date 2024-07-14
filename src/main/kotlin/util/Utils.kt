package com.webscrapper.util

object Utils {

    @JvmStatic
    fun handleDecimalSeparators(string: String): String {
        var modifiedString = string.replace(".", "")
        modifiedString = modifiedString.replace(",", ".")
        return modifiedString
    }
}
