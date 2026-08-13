package com.robertrussell.miguel.sendmoneydemoapp.util

import java.text.DecimalFormat

fun formatNumber(value: Number): String {
    val formatter = DecimalFormat("#,##0.00")
    return formatter.format(value)
}

fun String.maskNumbers(): String {
    return this.replace(Regex("\\d"), "*")
}
