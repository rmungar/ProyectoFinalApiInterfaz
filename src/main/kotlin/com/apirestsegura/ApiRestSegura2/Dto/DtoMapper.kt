package com.apirestsegura.ApiRestSegura2.Dto

import com.apirestsegura.ApiRestSegura2.Model.Usuario
import com.apirestsegura.ApiRestSegura2.Repository.UsuarioRepository
import com.apirestsegura.ApiRestSegura2.Service.UsuarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
object DtoMapper {

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository

    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder


    fun fromUsuarioDTOtoUsuario(usuario: UsuarioDTO): Usuario {
        TODO()
    }

    fun fromUsuarioToUsuarioDTO(usuario: Usuario): UsuarioDTO {
        TODO()
    }

    fun fromUsuarioRegisterDTOtoUsuario(registerDTO: UsuarioRegisterDTO): Usuario {
        TODO()
    }

    fun fromUsuarioToUsuarioRegisterDTO(usuario: Usuario): UsuarioRegisterDTO {
        TODO()
    }

}