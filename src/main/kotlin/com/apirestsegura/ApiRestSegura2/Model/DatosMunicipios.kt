package com.apirestsegura.ApiRestSegura2.Model

data class DatosMunicipios(
    val update_date: String,
    val size: Int,
    val data: List<Municipio>?,
    val warning: String?,
    val error: String?
) {
}