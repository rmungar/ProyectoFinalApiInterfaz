package com.apirestsegura.ApiRestSegura2.Dto

import com.apirestsegura.ApiRestSegura2.Model.Direccion


data class UsuarioRegisterDTO(
    val username: String,
    val email: String,
    var password: String,
    var passwordRepeat: String,
    val direccion: Direccion,
    val rol: String?
)
