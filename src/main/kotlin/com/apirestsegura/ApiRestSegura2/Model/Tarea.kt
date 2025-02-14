package com.apirestsegura.ApiRestSegura2.Model

data class Tarea(
    var _id: String? = null,
    val titulo: String,
    val descripcion: String,
    val usuario: Usuario,
) {
}