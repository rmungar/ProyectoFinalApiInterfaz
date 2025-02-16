package com.apirestsegura.ApiRestSegura2.Service

import com.apirestsegura.ApiRestSegura2.Repository.TareaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TareaService {

    @Autowired
    private lateinit var tareaRepository: TareaRepository

}