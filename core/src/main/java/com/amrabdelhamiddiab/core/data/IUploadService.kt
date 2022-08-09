package com.amrabdelhamiddiab.core.data

import com.amrabdelhamiddiab.core.domain.Service

interface IUploadService {
    suspend fun uploadService(service: Service):Boolean
}