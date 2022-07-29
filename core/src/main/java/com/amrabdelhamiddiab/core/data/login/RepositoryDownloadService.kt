package com.amrabdelhamiddiab.core.data.login

import com.amrabdelhamiddiab.core.data.IDownloadService

class RepositoryDownloadService(private val iDownloadService: IDownloadService) {
    suspend fun downloadService(userId: String) = iDownloadService.downloadService(userId)
}