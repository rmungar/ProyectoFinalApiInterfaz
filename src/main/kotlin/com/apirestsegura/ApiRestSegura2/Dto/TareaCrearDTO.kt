package com.example.interfazusuarioapi.Dto

import com.apirestsegura.ApiRestSegura2.Dto.UsuarioDTO
import com.apirestsegura.ApiRestSegura2.Model.Usuario
import java.util.Date

class TareaCrearDTO(
    var _id: Int? = null,
    val titulo: String,
    var estado: Boolean = false,
    val descripcion: String,
    val usuario: UsuarioDTO,
    val fechaProgramada: Date
) {

}