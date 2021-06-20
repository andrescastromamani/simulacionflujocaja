package com.acm.simulacionflujocaja

import java.lang.Double.parseDouble
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

class RedondeoDecimal {
    fun redondear(number: Double): Double {
        val decimal:Double = BigDecimal(number).setScale(2, RoundingMode.FLOOR).toDouble()
        /* val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.FLOOR
            return parseDouble(df.format(number))*/
        return decimal
    }
}