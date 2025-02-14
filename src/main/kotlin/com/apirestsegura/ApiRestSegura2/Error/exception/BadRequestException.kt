package com.apirestsegura.ApiRestSegura2.Error.exception

class BadRequestException(message: String) : Exception("Bad Request (400). $message") {
}