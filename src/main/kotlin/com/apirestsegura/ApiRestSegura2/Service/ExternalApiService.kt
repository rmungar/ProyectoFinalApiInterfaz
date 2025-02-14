package com.apirestsegura.ApiRestSegura2.Service

import com.apirestsegura.ApiRestSegura2.Model.DatosMunicipios
import com.apirestsegura.ApiRestSegura2.Model.DatosProvincias
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class ExternalApiService(private val webClient: WebClient.Builder) {

    @Value("\${API_KEY}")
    private lateinit var apiKey: String

    fun getProvinciasDeApi(): DatosProvincias? {
        return webClient.build()
            .get()
            .uri("https://apiv1.geoapi.es/provincias?type=JSON&key=$apiKey")
            .retrieve()
            .bodyToMono(DatosProvincias::class.java)
            .block() // ⚠️ Esto bloquea el hilo, usar `subscribe()` en código reactivo
    }

    fun getMunicipiosApi(CPRO: String): DatosMunicipios? {
        return webClient.build()
            .get()
            .uri("https://apiv1.geoapi.es/municipios?CPRO=$CPRO&type=JSON&key=$apiKey")
            .retrieve()
            .bodyToMono(DatosMunicipios::class.java)
            .block()
    }

}