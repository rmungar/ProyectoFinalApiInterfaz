package com.apirestsegura.ApiRestSegura2.Model


import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.mapping.Document

@Document("Usuario")
data class Usuario(
    @BsonId
    var _id : String?,
    val username: String,
    val password: String,
    val email: String,
    val direccion: Direccion,
    val roles: String = "USER"

){
    init {
        _id = email
    }
}