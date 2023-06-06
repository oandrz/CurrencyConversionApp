package com.example.testproject.ext

import java.math.RoundingMode
import java.text.DecimalFormat

fun Double.toDecimal(): String = DecimalFormat("#.##").let {
    it.roundingMode = RoundingMode.UP
    it.format(this)
}
