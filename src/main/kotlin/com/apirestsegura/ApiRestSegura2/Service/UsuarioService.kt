package com.apirestsegura.ApiRestSegura2.Service


import com.apirestsegura.ApiRestSegura2.Dto.UsuarioDTO
import com.apirestsegura.ApiRestSegura2.Dto.UsuarioRegisterDTO
import com.apirestsegura.ApiRestSegura2.Error.exception.BadRequestException
import com.apirestsegura.ApiRestSegura2.Error.exception.UserNotFoundException
import com.apirestsegura.ApiRestSegura2.Model.Usuario
import com.apirestsegura.ApiRestSegura2.Repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UsuarioService : UserDetailsService {

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var externalApiService: ExternalApiService

    override fun loadUserByUsername(username: String?): UserDetails {
        val usuario: Usuario = usuarioRepository
            .findByUsername(username!!)
            .orElseThrow {
                UserNotFoundException("No se encontró un usuario con el nombre: $username.")
            }

        return User.builder()
            .username(usuario.username)
            .password(usuario.password)
            .roles(usuario.rol)
            .build()
    }

    fun loadUserById(id: String?): UsuarioDTO {
        val usuario: Usuario = usuarioRepository
            .findByUsername(id!!)
            .orElseThrow {
                UserNotFoundException("No se encontró un usuario con el nombre: $id.")
            }

        return UsuarioDTO(
            username = usuario.username,
            rol = usuario.rol,
            email = usuario.email,
        )
    }

    fun insertUser(usuarioInsertadoDTO: UsuarioRegisterDTO) : UsuarioDTO? {
        var provinciaExistente = false
        var municipioDeProvincia = false
        if (usuarioInsertadoDTO.username.isBlank()) throw BadRequestException("El nombre del usuario no puede estar vacío.")
        if (usuarioInsertadoDTO.password.isBlank()) throw BadRequestException("La contraseña del usuario no puede estar vacía.")
        if (usuarioInsertadoDTO.email.isBlank()) throw BadRequestException("El email del usuario no puede estar vacío.")
        if (usuarioInsertadoDTO.passwordRepeat.isBlank()) throw BadRequestException("La contraseña del usuario no puede estar vacía.")
        if (usuarioInsertadoDTO.password != usuarioInsertadoDTO.passwordRepeat) throw BadRequestException("Las contraseñas ingresadas no coinciden.")
        if (usuarioInsertadoDTO.rol.isNullOrBlank()) throw BadRequestException("El rol del usuario no puede estar vacío o ser nulo.")
        if (usuarioInsertadoDTO.direccion.calle.isBlank()) throw BadRequestException("La calle no puede estar vacía.")
        try {
            usuarioInsertadoDTO.direccion.num.toInt()
            if (usuarioInsertadoDTO.direccion.num.isBlank()) throw BadRequestException("El número de la dirección no puede estar vacío.")
        }
        catch (e: Exception){
            throw BadRequestException("El número de la dirección no puede ser una letra.")
        }
        try {
            usuarioInsertadoDTO.direccion.cp.toInt()
            if (usuarioInsertadoDTO.direccion.num.isBlank()) throw BadRequestException("El código postal de la dirección no puede estar vacío.")
        }
        catch (e: Exception){
            throw BadRequestException("El código postal no puede ser una letra.")
        }
        val usuarioProv = usuarioInsertadoDTO.direccion.provincia.uppercase()
        val usuarioMunicipio = usuarioInsertadoDTO.direccion.municipio.uppercase()
        var CPRO = ""

        externalApiService.getProvinciasDeApi()?.data?.forEach {
            if (it.PRO == usuarioProv) {
                CPRO = it.CPRO
                provinciaExistente = true
            }
        }

        externalApiService.getMunicipiosApi(CPRO)?.data?.forEach {
            if (it.DMUN50 == usuarioMunicipio){
                municipioDeProvincia = true
            }
        }

        if (!provinciaExistente) throw BadRequestException("La provincia insertada no existe.")
        if (!municipioDeProvincia) throw BadRequestException("El municipio no pertenece a la provincia.")

        val usuarioAinsertar = Usuario(
            null,
            usuarioInsertadoDTO.username,
            passwordEncoder.encode(usuarioInsertadoDTO.password),
            usuarioInsertadoDTO.email,
            usuarioInsertadoDTO.direccion,
            usuarioInsertadoDTO.rol ?: "USER"
        )

        val usuarioExistente = usuarioRepository.findByUsername(usuarioAinsertar.username)
        if (usuarioExistente.isPresent) {
            throw BadRequestException("El usuario ya existe.")
        }
        usuarioRepository.insert(
            usuarioAinsertar
        )

        return UsuarioDTO(
            usuarioAinsertar.username,
            usuarioAinsertar.email,
            usuarioAinsertar.rol
        )

    }

    fun updateUser(usuarioInsertadoDTO: UsuarioRegisterDTO): UsuarioDTO? {

        val previousUserData = usuarioRepository.findById(usuarioInsertadoDTO.email).getOrNull()

        if (previousUserData == null) {
            throw UserNotFoundException("No se encontró un usuario con id: ${usuarioInsertadoDTO.email}")
        }
        else {
            previousUserData.username = usuarioInsertadoDTO.username
            previousUserData.password = passwordEncoder.encode(usuarioInsertadoDTO.password)
            previousUserData.direccion =  usuarioInsertadoDTO.direccion
            previousUserData.rol = usuarioInsertadoDTO.rol ?: "USER"

            usuarioRepository.save(previousUserData)

            return UsuarioDTO(
                previousUserData.username,
                previousUserData.email,
                previousUserData.rol,
            )
        }
    }

    fun deleteUser(usuarioId: String): Boolean {


        val userToDelete = usuarioRepository.findById(usuarioId).getOrNull()

        if (userToDelete == null) {
            throw UserNotFoundException(usuarioId)
        }
        else{
            try {
                usuarioRepository.deleteById(usuarioId)
                return true
            }
            catch (e: IllegalArgumentException) {
                return false
            }
        }
    }
}