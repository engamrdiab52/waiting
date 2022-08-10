package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryDownloadToken
import com.amrabdelhamiddiab.core.data.login.RepositoryListDownloadTokens

class ListDownloadTokens(private val repositoryListDownloadTokens: RepositoryListDownloadTokens) {
    suspend operator fun invoke() = repositoryListDownloadTokens.listDownloadTokens()
}