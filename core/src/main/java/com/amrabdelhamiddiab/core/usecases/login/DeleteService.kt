package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryDeleteService

class DeleteService (private val repositoryDeleteService: RepositoryDeleteService) {
    suspend operator fun invoke() =repositoryDeleteService.deleteService()
}
