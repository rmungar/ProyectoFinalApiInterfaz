package com.apirestsegura.ApiRestSegura2.Service

import com.apirestsegura.ApiRestSegura2.Dto.TareaDTO
import com.apirestsegura.ApiRestSegura2.Dto.TareaReturnDTO
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

    fun createTarea(tarea: TareaCrearDTO): TareaReturnDTO {
        if (tarea.titulo.isBlank()) throw BadRequestException("El título de la tarea no puede estar vacío.")
        if (tarea.descripcion.isBlank()) throw BadRequestException("La descripción de la tarea no puede estar vacía.")
        if (tarea.estado) throw BadRequestException("La tarea no puede estar completada.")
        val usuarioExistente = usuarioRepository.findByUsername(tarea.usuario.username)
        if (!usuarioExistente.isPresent) {
            throw BadRequestException("El usuario debe existir en la base de datos.")
        }
        val tareas = tareaRepository.findAll()
        for (tareas in tareas) {
            if (tareas.titulo == tarea.titulo && tareas.usuario == tarea.usuario) {
                throw BadRequestException("Ya hay una tarea con ese id.")
            }
        }

        val task = Tarea(
            null,
            tarea.titulo,
            tarea.estado,
            tarea.descripcion,
            tarea.usuario
        )

        tareaRepository.save(task)

        return TareaReturnDTO(
            _id = tarea._id!!,
            titulo = tarea.titulo,
            estado = tarea.estado,
            usuario = tarea.usuario.username,
        )
    }

    fun marcarCompletada(idTarea: String): TareaReturnDTO {
        val tareaExistente = tareaRepository.findById(idTarea).getOrNull()
        if (tareaExistente != null) {
            tareaExistente.estado = true
            tareaRepository.save(tareaExistente)
            return TareaReturnDTO(
                tareaExistente._id!!,
                tareaExistente.titulo,
                tareaExistente.estado,
                tareaExistente.usuario.username
            )
        }
        else{
            throw BadRequestException("No existe una tarea con ese id.")
        }
    }

    fun getTareas(): List<TareaReturnDTO> {

        val tareas = tareaRepository.findAll()
        if (tareas.isNotEmpty()) {
            return tareas.map { TareaReturnDTO(it._id!!, it.titulo, it.estado, it.usuario.username) }
        }
        else {
            return emptyList()
        }

    }

    fun getTareas(nombreUsuario: String): List<TareaReturnDTO> {

        val usuarioExistente = usuarioRepository.findByUsername(nombreUsuario).getOrNull()
        if (usuarioExistente != null) {
            val tareas = tareaRepository.findAll().filter { it.usuario.username.uppercase() == nombreUsuario.uppercase() }
            if (tareas.isNotEmpty()) {
                return tareas.map { TareaReturnDTO(it._id!!, it.titulo, it.estado, it.usuario.username) }
            }
            else {
                return emptyList()
            }
        }
        else {
            throw BadRequestException("No existe ese usuario.")
        }
    }


    fun getTarea(idTarea: String): TareaReturnDTO {
        val tareaExistente = tareaRepository.findByIdOrNull(idTarea)
        if (tareaExistente != null) {
            return TareaReturnDTO(
                tareaExistente._id!!,
                tareaExistente.titulo,
                tareaExistente.estado,
                tareaExistente.usuario.username,
            )
        }
        else {
            throw BadRequestException("No existe una tarea con ese id.")
        }
    }


    fun getTareaIdByData(tarea:TareaDTO): String{
        val tareaExistente = tareaRepository.findAll().filter {
            it.estado == tarea.estado &&
            it.usuario.username == tarea.usuario
        }.firstOrNull()

        if (tareaExistente != null) {
            return tareaExistente._id ?: ""
        }
        else{
            throw BadRequestException("No existe tarea con esas condiciones.")
        }
    }


    fun deleteTarea(idTarea: String): Boolean {
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