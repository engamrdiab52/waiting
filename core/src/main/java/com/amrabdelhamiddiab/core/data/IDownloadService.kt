package com.amrabdelhamiddiab.core.data

import com.amrabdelhamiddiab.core.domain.Service

interface IDownloadService {
    suspend fun downloadService(userId: String): Service?
}