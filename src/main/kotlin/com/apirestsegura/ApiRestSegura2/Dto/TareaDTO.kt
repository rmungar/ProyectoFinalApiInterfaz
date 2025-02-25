package com.apirestsegura.ApiRestSegura2.Dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

data class TareaDTO(
    val titulo: String,
    val estado: Boolean,
    val usuario: String)
