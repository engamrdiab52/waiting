package com.amrabdelhamiddiab.core.data.login

class RepositoryUploadClientToken(private val iUploadClientToken: IUploadClientToken) {
    suspend fun uploadTokenValue(userId: String, token: String) =
        iUploadClientToken.uploadTokenValue(userId, token)
}