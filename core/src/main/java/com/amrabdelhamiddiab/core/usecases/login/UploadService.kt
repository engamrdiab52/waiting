package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryUploadService
import com.amrabdelhamiddiab.core.domain.Service

class UploadService(private val repositoryUploadService: RepositoryUploadService) {
    suspend operator fun invoke(service: Service) =
        repositoryUploadService.uploadService(service)

}