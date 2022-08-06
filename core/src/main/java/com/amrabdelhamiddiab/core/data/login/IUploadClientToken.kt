package com.amrabdelhamiddiab.core.data.login

import com.amrabdelhamiddiab.core.domain.Token

interface IUploadClientToken {
    suspend fun uploadTokenValue(userId: String, token: Token) :Boolean
}