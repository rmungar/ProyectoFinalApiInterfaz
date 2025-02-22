package com.apirestsegura.ApiRestSegura2.Controller

import com.apirestsegura.ApiRestSegura2.Error.exception.BadRequestException
import com.apirestsegura.ApiRestSegura2.Error.exception.UnauthorizedException
import com.apirestsegura.ApiRestSegura2.Model.Tarea
import com.apirestsegura.ApiRestSegura2.Service.TareaService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.repository.Update
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tareas")
class TareaController {


    @Autowired
    private lateinit var tareaService: TareaService



    @PostMapping("/crear")
    fun addTarea(
        httpServletRequest: HttpServletRequest,
        @RequestBody tarea: Tarea?
    ): ResponseEntity<Any>? {

        try {
            if (tarea != null) {
                val result = tareaService.createTarea(tarea)
                return ResponseEntity(result, HttpStatus.CREATED)
            }
            else{
                return ResponseEntity(null, HttpStatus.BAD_REQUEST)
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


    @GetMapping
    fun getTareas():ResponseEntity<List<Tarea>?>{
        TODO()
    }


    @PutMapping("/marcar/{idTarea}")
    fun marcarCompletada(
        @PathVariable idTarea: Int?,
    ): ResponseEntity<Any>?{
        try {
            if (idTarea != null) {
                val result = tareaService.marcarCompletada(idTarea)
                return ResponseEntity(result, HttpStatus.OK)
            }
            else{
                return ResponseEntity(null, HttpStatus.BAD_REQUEST)
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