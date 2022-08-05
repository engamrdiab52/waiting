package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryUploadClientToken

class UploadClientToken(private val repositoryUploadClientToken: RepositoryUploadClientToken) {
    suspend operator fun invoke(userId: String, token: String) =
        repositoryUploadClientToken.uploadTokenValue(userId, token)
}