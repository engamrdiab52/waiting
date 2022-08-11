package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryDeleteService
import com.amrabdelhamiddiab.core.data.login.RepositoryDeleteThisDay

class DeleteThisDay(private val repositoryDeleteThisDay: RepositoryDeleteThisDay) {
    suspend operator fun invoke() =repositoryDeleteThisDay.deleteThisDay()
}
