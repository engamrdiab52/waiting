package com.amrabdelhamiddiab.core.data.login

interface IDeleteCurrentOrder {
    suspend fun deleteCurrentOrder(userId: String)
}