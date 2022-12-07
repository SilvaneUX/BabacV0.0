package com.smart.smartbalibackpaker.core.utils

import java.text.NumberFormat
import java.util.*

object Formatter {
    fun rupiahFormatter(number: Double): String{
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(number)
    }
}