package com.apirestsegura.ApiRestSegura2.Repository

import com.apirestsegura.ApiRestSegura2.Model.Tarea
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TareaRepository: MongoRepository<Tarea, Int> {



}