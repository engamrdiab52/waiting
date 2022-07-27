package com.amrabdelhamiddiab.core.data

import com.amrabdelhamiddiab.core.domain.Service

interface IUploadService {
    suspend fun uploadService(userId: String, service: Service):Boolean
}