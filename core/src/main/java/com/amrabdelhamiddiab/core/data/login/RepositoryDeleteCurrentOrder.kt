package com.amrabdelhamiddiab.core.data.login

class RepositoryDeleteCurrentOrder(private val iDeleteCurrentOrder: IDeleteCurrentOrder) {
    suspend fun deleteCurrentOrder(userId: String) = iDeleteCurrentOrder.deleteCurrentOrder(userId)
}