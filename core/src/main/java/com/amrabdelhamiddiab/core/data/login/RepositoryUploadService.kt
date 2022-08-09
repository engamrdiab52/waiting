package com.amrabdelhamiddiab.core.data.login

import com.amrabdelhamiddiab.core.data.IUploadService
import com.amrabdelhamiddiab.core.domain.Service

class RepositoryUploadService(private val iUploadService: IUploadService) {
    suspend fun uploadService(service: Service) =
        iUploadService.uploadService( service)
}