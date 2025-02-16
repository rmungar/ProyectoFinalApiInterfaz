package com.apirestsegura.ApiRestSegura2.Model


import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.mapping.Document

@Document("Usuarios")
data class Usuario(
    @BsonId
    var _id : String?,
    var username: String,
    var password: String,
    val email: String,
    var direccion: Direccion,
    var roles: String = "USER"

){
    init {
        _id = email
    }
}