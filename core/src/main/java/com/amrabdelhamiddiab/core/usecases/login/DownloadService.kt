package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryDownloadService

class DownloadService(private val repositoryDownloadService: RepositoryDownloadService) {
    suspend operator fun invoke(userId: String) = repositoryDownloadService.downloadService(userId)
}