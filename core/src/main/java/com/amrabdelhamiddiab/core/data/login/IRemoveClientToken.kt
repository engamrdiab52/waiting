package com.amrabdelhamiddiab.core.data.login

interface IRemoveClientToken {
    suspend fun removeClientToken() : Boolean
}