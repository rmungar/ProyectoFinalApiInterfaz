package com.apirestsegura.ApiRestSegura2.Model

import java.util.Date

data class Tarea(
    var _id: String? = null,
    val titulo: String,
    val descripcion: String,
    val usuario: Usuario,
    val fechaProgramada: Date
) {
}