package com.amrabdelhamiddiab.core.data.login

interface IDeleteService {
    suspend fun deleteService(userId: String)
}