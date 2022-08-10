package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryUploadClientToken
import com.amrabdelhamiddiab.core.domain.Token

class UploadClientToken(private val repositoryUploadClientToken: RepositoryUploadClientToken) {
    suspend operator fun invoke(token: Token) =
        repositoryUploadClientToken.uploadTokenValue( token)
}