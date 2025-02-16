package com.apirestsegura.ApiRestSegura2.Controller

import com.apirestsegura.ApiRestSegura2.Model.Tarea
import com.apirestsegura.ApiRestSegura2.Service.TareaService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tareas")
class TareaController {


    @Autowired
    private lateinit var tareaService: TareaService



    @PostMapping
    fun addTarea(
        httpServletRequest: HttpServletRequest,
        @RequestBody tarea: Tarea,
        @RequestParam(value = "_idUsuario") idUsuario: String
    ): ResponseEntity<Tarea?> {


        TODO()


    }


    @GetMapping
    fun getTareas():ResponseEntity<List<Tarea>?>{
        TODO()
    }

}