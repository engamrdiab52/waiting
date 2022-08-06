package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryUploadClientToken
import com.amrabdelhamiddiab.core.domain.Token

class UploadClientToken(private val repositoryUploadClientToken: RepositoryUploadClientToken) {
    suspend operator fun invoke(userId: String, token: Token) =
        repositoryUploadClientToken.uploadTokenValue(userId, token)
}