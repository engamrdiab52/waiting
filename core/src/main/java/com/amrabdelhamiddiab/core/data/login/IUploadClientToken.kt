package com.amrabdelhamiddiab.core.data.login

interface IUploadClientToken {
    suspend fun uploadTokenValue(userId: String, token: String) :Boolean
}