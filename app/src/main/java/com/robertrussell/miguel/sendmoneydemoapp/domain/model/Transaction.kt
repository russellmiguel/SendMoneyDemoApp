package com.robertrussell.miguel.sendmoneydemoapp.domain.model

data class Transaction(
    val id: Int,
    val amount: Double,
    val recipient: String,
    val date: Long,
    val type: String
)
