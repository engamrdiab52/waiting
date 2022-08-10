package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryDownloadOrder
import com.amrabdelhamiddiab.core.data.login.RepositoryDownloadToken

class DownloadToken(private val repositoryDownloadToken: RepositoryDownloadToken) {
    suspend operator fun invoke() = repositoryDownloadToken.downloadToken()
}