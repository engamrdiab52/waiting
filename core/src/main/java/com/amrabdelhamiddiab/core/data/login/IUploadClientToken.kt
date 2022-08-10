package com.amrabdelhamiddiab.core.data.login

import com.amrabdelhamiddiab.core.domain.Token

interface IUploadClientToken {
    suspend fun uploadTokenValue(token: Token) :Boolean
}