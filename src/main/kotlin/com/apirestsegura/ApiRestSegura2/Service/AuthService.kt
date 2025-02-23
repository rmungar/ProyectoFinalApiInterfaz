package com.apirestsegura.ApiRestSegura2.Service

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthService {

    fun getUsernameFromToken(): String? {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication

        if (authentication is JwtAuthenticationToken) {
            val jwt: Jwt = authentication.token
            return jwt.claims["username"]?.toString() ?: jwt.claims["sub"]?.toString()
        }

        return null
    }


    fun getRolFromToken(): String? {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        if (authentication == null || !authentication.isAuthenticated) {
            return null
        }

        if (authentication is JwtAuthenticationToken) {
            val jwt: Jwt = authentication.token
            return jwt.claims["roles"]?.toString() ?: jwt.claims["role"]?.toString() ?: jwt.claims["authorities"]?.toString()
        }

        return null
    }



}
