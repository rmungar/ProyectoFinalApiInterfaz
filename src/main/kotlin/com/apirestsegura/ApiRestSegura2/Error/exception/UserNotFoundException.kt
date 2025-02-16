package com.apirestsegura.ApiRestSegura2.Error.exception

class UserNotFoundException(_id: String): Exception("Usuario con _id: $_id no encontrado.") {
}