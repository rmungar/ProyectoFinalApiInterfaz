package com.apirestsegura.ApiRestSegura2.Model

import com.apirestsegura.ApiRestSegura2.Dto.UsuarioDTO
import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document("Tareas")
data class Tarea(
    @BsonId
    var _id: Int? = null,
    val titulo: String,
    var estado: Boolean = false,
    val descripcion: String,
    val usuario: UsuarioDTO,
    val fechaProgramada: Date
) {

    companion object {
        var cont = 0
    }

    init {
        if (_id == null){
            _id = ++cont
        }
    }
}