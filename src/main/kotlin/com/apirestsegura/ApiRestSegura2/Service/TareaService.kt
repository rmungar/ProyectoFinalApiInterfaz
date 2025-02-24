package com.apirestsegura.ApiRestSegura2.Service

import com.apirestsegura.ApiRestSegura2.Dto.TareaDTO
import com.apirestsegura.ApiRestSegura2.Error.exception.BadRequestException
import com.apirestsegura.ApiRestSegura2.Model.Tarea
import com.apirestsegura.ApiRestSegura2.Repository.TareaRepository
import com.apirestsegura.ApiRestSegura2.Repository.UsuarioRepository
import com.example.interfazusuarioapi.Dto.TareaCrearDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class TareaService {

    @Autowired
    private lateinit var tareaRepository: TareaRepository

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository

    fun createTarea(tarea: TareaCrearDTO): TareaDTO {
        if (tarea.titulo.isBlank()) throw BadRequestException("El título de la tarea no puede estar vacío.")
        if (tarea.descripcion.isBlank()) throw BadRequestException("La descripción de la tarea no puede estar vacía.")
        if (tarea.estado) throw BadRequestException("La tarea no puede estar completada.")
        val usuarioExistente = usuarioRepository.findByUsername(tarea.usuario.username)
        if (!usuarioExistente.isPresent) {
            throw BadRequestException("El usuario debe existir en la base de datos.")
        }
        if (tarea.fechaProgramada.before(Date.from(Instant.now()))) throw BadRequestException("La fecha debe de ser posterior al momento de asignación.")


        val task = Tarea(
            tarea._id,
            tarea.titulo,
            tarea.estado,
            tarea.descripcion,
            tarea.usuario,
            tarea.fechaProgramada,
        )

        tareaRepository.save(task)

        return TareaDTO(
            titulo = tarea.titulo,
            estado = tarea.estado,
            usuario = tarea.usuario.username,
            fechaProgramada = tarea.fechaProgramada
        )
    }

    fun marcarCompletada(idTarea: Int): TareaDTO {
        val tareaExistente = tareaRepository.findById(idTarea).getOrNull()
        if (tareaExistente != null) {
            tareaExistente.estado = true
            tareaRepository.save(tareaExistente)
            return TareaDTO(
                tareaExistente.titulo,
                tareaExistente.estado,
                tareaExistente.usuario.username,
                tareaExistente.fechaProgramada
            )
        }
        else{
            throw BadRequestException("No existe una tarea con ese id.")
        }
    }


    fun getTareas(): List<TareaDTO> {

        val tareas = tareaRepository.findAll()
        if (tareas.isNotEmpty()) {
            return tareas.map { TareaDTO(it.titulo, it.estado, it.usuario.username, it.fechaProgramada) }
        }
        else {
            return emptyList()
        }

    }

    fun getTareas(nombreUsuario: String): List<TareaDTO> {

        val usuarioExistente = usuarioRepository.findByUsername(nombreUsuario).getOrNull()
        if (usuarioExistente != null) {
            val tareas = tareaRepository.findAll().filter { it.usuario.username.uppercase() == nombreUsuario.uppercase() }
            if (tareas.isNotEmpty()) {
                return tareas.map { TareaDTO(it.titulo, it.estado, it.usuario.username, it.fechaProgramada) }
            }
            else {
                return emptyList()
            }
        }
        else {
            throw BadRequestException("No existe ese usuario.")
        }
    }


    fun getTarea(idTarea: Int): TareaDTO {
        val tareaExistente = tareaRepository.findByIdOrNull(idTarea)
        if (tareaExistente != null) {
            return TareaDTO(
                tareaExistente.titulo,
                tareaExistente.estado,
                tareaExistente.usuario.username,
                tareaExistente.fechaProgramada
            )
        }
        else {
            throw BadRequestException("No existe una tarea con ese id.")
        }
    }


    fun deleteTarea(idTarea: Int): Boolean {
        val tareaExistente = tareaRepository.findByIdOrNull(idTarea)
        if (tareaExistente != null) {
            tareaRepository.delete(tareaExistente)
            return true
        }
        else {
            throw BadRequestException("No existe una tarea con ese id.")
        }
    }

}