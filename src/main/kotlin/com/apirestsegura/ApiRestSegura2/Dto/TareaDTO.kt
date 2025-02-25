package com.apirestsegura.ApiRestSegura2.Dto

import java.util.*

data class TareaDTO(
    val titulo: String,
    val estado: Boolean,
    val usuario: String,
    val fechaProgramada: Date) {
}