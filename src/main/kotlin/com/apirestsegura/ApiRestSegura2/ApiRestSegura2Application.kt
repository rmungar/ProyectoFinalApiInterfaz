package com.apirestsegura.ApiRestSegura2

import com.apirestsegura.ApiRestSegura2.Security.RSAKeysProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(RSAKeysProperties::class)
class ApiRestSegura2Application

fun main(args: Array<String>) {
	runApplication<ApiRestSegura2Application>(*args)
}
