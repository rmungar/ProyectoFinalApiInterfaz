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
import org.springframework.web.bind.annotation.*

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
        httpRequest: HttpServletRequest,
        @RequestBody usuario: LoginUsuarioDTO
    ) : ResponseEntity<Any>? {

        val authentication: Authentication
        try {

            authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(usuario.username, usuario.password))
            val token = tokenService.generarToken(authentication)
            return ResponseEntity(mapOf("token" to token), HttpStatus.CREATED)

        } catch (e: AuthenticationException) {
            return ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
        }
        catch (e: Exception) {
            return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/update")
    fun updateUser(
        httpRequest: HttpServletRequest,
        @RequestBody userData: UsuarioRegisterDTO
    ): ResponseEntity<Any>? {
        try {
            val result = usuarioService.updateUser(userData)
            return if (result != null) {
                ResponseEntity(result, HttpStatus.OK)
            } else {
                ResponseEntity("No se pudo actualizar el usuario.", HttpStatus.BAD_REQUEST)
            }
        }
        catch (e: AuthenticationException) {
            return ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
        }
        catch (e: Exception) {
            return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/delete/{usuarioId}")
    fun deleteUser(
        httpRequest: HttpServletRequest,
        @PathVariable("usuarioId") usuarioId: String?
    ): ResponseEntity<Any>? {
        try {
            if (usuarioId != null) {
                val result = usuarioService.deleteUser(usuarioId)
                return if (result) {
                    ResponseEntity("Usuario borrado correctamente.", HttpStatus.OK)
                } else ResponseEntity("No existe un usuario con ese id", HttpStatus.BAD_REQUEST)
            }
            else {
                return ResponseEntity("El id de usuario no puede ser nulo.", HttpStatus.BAD_REQUEST)
            }
        }
        catch (e: UnauthorizedException) {
            return ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
        }
        catch (e: Exception) {
            return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/usuario/{id}")
    fun getUser(
        httpRequest: HttpServletRequest,
        @PathVariable("id") usuarioId: String?
    ): ResponseEntity<Any>? {
        try {
            if (usuarioId != null) {
                val result = usuarioService.loadUserById(id = usuarioId)
                return ResponseEntity(result, HttpStatus.OK)
            }
            else{
                return ResponseEntity("No se pudo obtener el usuario.", HttpStatus.BAD_REQUEST)
            }
        }
        catch (e: UnauthorizedException) {
            return ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
        }
        catch (e: Exception) {
            return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}