package com.apirestsegura.ApiRestSegura2.Service

import com.apirestsegura.ApiRestSegura2.Dto.TareaDTO
import com.apirestsegura.ApiRestSegura2.Error.exception.BadRequestException
import com.apirestsegura.ApiRestSegura2.Model.Tarea
import com.apirestsegura.ApiRestSegura2.Repository.TareaRepository
import com.apirestsegura.ApiRestSegura2.Repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class TareaService {

    @Autowired
    private lateinit var tareaRepository: TareaRepository

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository

    fun createTarea(tarea: Tarea): TareaDTO {
        if (tarea.titulo.isBlank()) throw BadRequestException("El título de la tarea no puede estar vacío.")
        if (tarea.descripcion.isBlank()) throw BadRequestException("La descripción de la tarea no puede estar vacía.")
        if (tarea.estado) throw BadRequestException("La tarea no puede estar completada.")
        val usuarioExistente = usuarioRepository.findByUsername(tarea.usuario.username)
        if (!usuarioExistente.isPresent) {
            throw BadRequestException("El usuario debe existir en la base de datos.")
        }
        if (tarea.fechaProgramada.before(Date.from(Instant.now()))) throw BadRequestException("La fecha debe de ser posterior al momento de asignación.")

        tareaRepository.save(tarea)

        return TareaDTO(
            titulo = tarea.titulo,
            estado = tarea.estado
        )
    }
}