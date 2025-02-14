package com.apirestsegura.ApiRestSegura2.Model

data class Direccion(
    val calle: String,
    val num: Int,
    val municipio: String,
    val provincia: String,
    val cp: Int
) {
}