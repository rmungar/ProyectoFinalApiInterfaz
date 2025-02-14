package com.apirestsegura.ApiRestSegura2.Error.exception

class UnauthorizedException(message: String) : Exception("Not authorized exception (401). $message") {
}