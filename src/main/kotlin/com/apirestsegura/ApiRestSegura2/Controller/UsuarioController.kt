package com.apirestsegura.ApiRestSegura2.Controller

import com.apirestsegura.ApiRestSegura2.Dto.LoginUsuarioDTO
import com.apirestsegura.ApiRestSegura2.Dto.UsuarioRegisterDTO
import com.apirestsegura.ApiRestSegura2.Error.exception.BadRequestException
import com.apirestsegura.ApiRestSegura2.Error.exception.UnauthorizedException
import com.apirestsegura.ApiRestSegura2.Service.TokenService
import com.apirestsegura.ApiRestSegura2.Service.UsuarioService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
@RequestMapping("/usuarios")
@RestController
class UsuarioController {

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager
    @Autowired
    private lateinit var tokenService: TokenService
    @Autowired
    private lateinit var usuarioService: UsuarioService

    @PostMapping("/register")
    fun insert(
        httpRequest: HttpServletRequest,
        @RequestBody usuarioRegisterDTO: UsuarioRegisterDTO
    ) : ResponseEntity<Any>?{

        try {
            val usuarioRegister = UsuarioRegisterDTO(
                username = usuarioRegisterDTO.username,
                email = usuarioRegisterDTO.email,
                password = usuarioRegisterDTO.password,
                passwordRepeat = usuarioRegisterDTO.passwordRepeat,
                direccion = usuarioRegisterDTO.direccion,
                rol = usuarioRegisterDTO.rol

            )

            val result = usuarioService.insertUser(usuarioRegister)

            return ResponseEntity(result, HttpStatus.CREATED)

        }
        catch (e: UnauthorizedException) {
            return ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
        }
        catch (e: BadRequestException) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }
        catch (e: Exception) {
            return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }

    @PostMapping("/login")
    fun login(
        @RequestBody usuario: LoginUsuarioDTO
    ) : ResponseEntity<Any>? {

        val authentication: Authentication
        try {
            authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(usuario.username, usuario.password))
        } catch (e: AuthenticationException) {
            throw UnauthorizedException("Credenciales incorrectas")
        }

        // SI PASAMOS LA AUTENTICACIÓN, SIGNIFICA QUE ESTAMOS BIEN AUTENTICADOS
        // PASAMOS A GENERAR EL TOKEN
        var token = tokenService.generarToken(authentication)

        return ResponseEntity(mapOf("token" to token), HttpStatus.CREATED)
    }

}