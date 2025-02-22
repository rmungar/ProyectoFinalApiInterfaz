package com.apirestsegura.ApiRestSegura2.Model

import com.apirestsegura.ApiRestSegura2.Dto.UsuarioDTO
import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document("Tareas")
data class Tarea(
    @BsonId
    var _id: String? = null,
    val titulo: String,
    var estado: Boolean = false,
    val descripcion: String,
    val usuario: UsuarioDTO,
    val fechaProgramada: Date
) {
}