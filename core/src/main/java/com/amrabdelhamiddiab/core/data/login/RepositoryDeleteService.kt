package com.amrabdelhamiddiab.core.data.login

class RepositoryDeleteService(private val iDeleteService: IDeleteService) {
    suspend fun deleteService(userId: String) = iDeleteService.deleteService(userId)
}