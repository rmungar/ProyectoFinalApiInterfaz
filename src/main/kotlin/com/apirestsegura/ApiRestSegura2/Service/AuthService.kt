package com.apirestsegura.ApiRestSegura2.Service

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service

@Service
class AuthService {

    fun getUsernameFromToken(): String? {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        if (authentication?.principal is Jwt) {
            val jwt: Jwt = authentication.principal as Jwt
            return jwt.claims["username"]?.toString()
        }
        return null
    }

    fun getRolFromToken(): String? {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        if (authentication?.principal is Jwt) {
            val jwt: Jwt = authentication.principal as Jwt
            return jwt.claims["rol"]?.toString()
        }
        return null
    }


}
