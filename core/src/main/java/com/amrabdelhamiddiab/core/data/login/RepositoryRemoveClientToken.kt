package com.amrabdelhamiddiab.core.data.login

class RepositoryRemoveClientToken(private val iRemoveClientToken: IRemoveClientToken) {
    suspend fun removeClientToken() = iRemoveClientToken.removeClientToken()
}