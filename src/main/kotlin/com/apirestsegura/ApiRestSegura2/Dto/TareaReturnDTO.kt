package com.apirestsegura.ApiRestSegura2.Dto

data class TareaReturnDTO(
    val _id: String,
    val titulo: String,
    val estado: Boolean,
    val usuario: String
) {
}