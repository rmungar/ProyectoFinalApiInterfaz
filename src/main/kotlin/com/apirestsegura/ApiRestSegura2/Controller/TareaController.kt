package com.apirestsegura.ApiRestSegura2.Controller

import com.apirestsegura.ApiRestSegura2.Error.exception.BadRequestException
import com.apirestsegura.ApiRestSegura2.Error.exception.UnauthorizedException
import com.apirestsegura.ApiRestSegura2.Model.Tarea
import com.apirestsegura.ApiRestSegura2.Service.AuthService
import com.apirestsegura.ApiRestSegura2.Service.TareaService
import com.example.interfazusuarioapi.Dto.TareaCrearDTO
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.text.ParseException

@RestController
@RequestMapping("/tareas")
class TareaController {


    @Autowired
    private lateinit var tareaService: TareaService

    @Autowired
    private lateinit var authService: AuthService


    @PostMapping("/crear")
    fun addTarea(
        httpServletRequest: HttpServletRequest,
        @RequestBody tarea: TareaCrearDTO?
    ): ResponseEntity<Any>? {

        try {
            if (tarea != null) {

                val usuarioActual = authService.getUsernameFromToken()
                if (usuarioActual != null) {
                    if (usuarioActual != tarea.usuario.username) {
                        val rolUsuario = authService.getRolFromToken()
                        if (rolUsuario != null && rolUsuario == "ROLE_ADMIN") {
                            val result = tareaService.createTarea(tarea)
                            return ResponseEntity(result, HttpStatus.CREATED)
                        }
                        else {
                            return ResponseEntity("No puedes añadirle una tarea a otro usuario.", HttpStatus.FORBIDDEN)
                        }
                    }
                    else{
                        val result = tareaService.createTarea(tarea)
                        return ResponseEntity(result, HttpStatus.CREATED)
                    }
                }
                else{
                    return ResponseEntity("El usuario no puede ser nulo.", HttpStatus.UNAUTHORIZED)
                }
            }
            else{
                return ResponseEntity("La tarea no puede ser nula.", HttpStatus.BAD_REQUEST)
            }
        }
        catch (e: UnauthorizedException) {
            return ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
        }
        catch (e: BadRequestException) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }
        catch (e: ParseException) {
            return ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }


    @GetMapping("/obtener")
    fun getTareas():ResponseEntity<Any>{
        try {
            val rolUsuarioActual = authService.getRolFromToken()
            val nombreUsuarioActual = authService.getUsernameFromToken()
            if (rolUsuarioActual != null) {
                if (rolUsuarioActual == "ROLE_ADMIN") {
                    val result = tareaService.getTareas()
                    if (result.isNotEmpty()) return ResponseEntity(result, HttpStatus.OK)
                    else return ResponseEntity("No se encontraron tareas en la base de datos.", HttpStatus.NOT_FOUND)
                }
                else {
                    if (nombreUsuarioActual != null) {
                        val result = tareaService.getTareas(nombreUsuarioActual)
                        if (result.isNotEmpty()) return ResponseEntity(result, HttpStatus.OK)
                        else return ResponseEntity("No se encontraron tareas en la base de datos para el usuario.", HttpStatus.NOT_FOUND)
                    }
                    else{
                        return ResponseEntity("El usuario no puede se nulo.", HttpStatus.BAD_REQUEST)
                    }
                }
            }
            else{
                return ResponseEntity("El usuario no puede no tener un rol.", HttpStatus.BAD_REQUEST)
            }
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


    @PutMapping("/marcar/{idTarea}")
    fun marcarCompletada(
        @PathVariable idTarea: Int?,
    ): ResponseEntity<Any>?{
        try {
            if (idTarea != null) {

                val usuarioActual = authService.getUsernameFromToken()

                val tareaActual = tareaService.getTarea(idTarea)

                if (usuarioActual != null) {
                    if (usuarioActual != tareaActual.usuario) {
                        val rolUsuario = authService.getRolFromToken()
                        if (rolUsuario != null && rolUsuario == "ROLE_ADMIN") {
                            val result = tareaService.marcarCompletada(idTarea)
                            return ResponseEntity(result, HttpStatus.OK)
                        }
                        else {
                            return ResponseEntity("No puedes modificar las tareas de otro usuario.", HttpStatus.FORBIDDEN)
                        }
                    }
                    else {
                        val result = tareaService.marcarCompletada(idTarea)
                        return ResponseEntity(result, HttpStatus.OK)
                    }
                }
                else {
                    return ResponseEntity("El usuario no puede ser nulo.", HttpStatus.BAD_REQUEST)
                }
            }
            else{
                return ResponseEntity("El id de la tarea no puede ser nulo.", HttpStatus.BAD_REQUEST)
            }
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


    @DeleteMapping("/eliminar/{idTarea}")
    fun eliminarTarea(
        @PathVariable idTarea: Int?,
    ): ResponseEntity<Any>{
        try {
            if (idTarea != null) {
                val rolUsuarioActual = authService.getRolFromToken()
                val nombreUsuarioActual = authService.getUsernameFromToken()
                val tareaActual = tareaService.getTarea(idTarea)

                if (rolUsuarioActual != null) {
                    if (nombreUsuarioActual != tareaActual.usuario) {
                        if (rolUsuarioActual == "ROLE_ADMIN") {
                            val result = tareaService.deleteTarea(idTarea)
                            if (result) return ResponseEntity("La tarea se ha borrado correctamente.", HttpStatus.OK)
                            else return ResponseEntity("No se pudo eliminar la tarea.", HttpStatus.BAD_REQUEST)
                        }
                        else{
                            return ResponseEntity("No puedes eliminar las tareas de otro usuario.", HttpStatus.FORBIDDEN)
                        }
                    }
                    else {
                        val result = tareaService.deleteTarea(idTarea)
                        if (result) return ResponseEntity("La tarea se ha borrado correctamente.", HttpStatus.OK)
                        else return ResponseEntity("No se pudo eliminar la tarea.", HttpStatus.BAD_REQUEST)
                    }
                }
                else{
                    return ResponseEntity("El usuario no puede no tener un rol.", HttpStatus.BAD_REQUEST)
                }
            }
            else{
                return ResponseEntity("El id de la tarea no puede ser nulo.", HttpStatus.BAD_REQUEST)
            }
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
}