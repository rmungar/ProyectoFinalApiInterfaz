package com.apirestsegura.ApiRestSegura2.Model

import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document("Tareas")
data class Tarea(
    @BsonId
    var _id: String? = null,
    val titulo: String,
    val descripcion: String,
    val usuario: Usuario,
    val fechaProgramada: Date
) {
}