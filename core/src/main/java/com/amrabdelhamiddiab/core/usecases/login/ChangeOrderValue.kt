package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryChangeOrderValue

class ChangeOrderValue(private val repositoryChangeOrderValue: RepositoryChangeOrderValue) {
    suspend operator fun invoke(value: Int) = repositoryChangeOrderValue.changeOrderValue(value)
}