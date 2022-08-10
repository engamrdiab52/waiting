package com.amrabdelhamiddiab.core.data.login

import com.amrabdelhamiddiab.core.domain.Token

class RepositoryUploadClientToken(private val iUploadClientToken: IUploadClientToken) {
    suspend fun uploadTokenValue( token: Token) =
        iUploadClientToken.uploadTokenValue(token)
}