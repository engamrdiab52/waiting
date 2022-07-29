package com.amrabdelhamiddiab.core.data

import com.amrabdelhamiddiab.core.domain.Service

interface IDownloadService {
    suspend fun downloadService(string: String): Service?
}