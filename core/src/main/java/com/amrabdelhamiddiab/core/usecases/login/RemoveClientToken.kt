package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryDownloadService
import com.amrabdelhamiddiab.core.data.login.RepositoryRemoveClientToken

class RemoveClientToken(private val repositoryRemoveClientToken: RepositoryRemoveClientToken) {
    suspend operator fun invoke() = repositoryRemoveClientToken.removeClientToken()
}