package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryChangeOrderValue
import com.amrabdelhamiddiab.core.domain.Order


class ChangeOrderValue(private val repositoryChangeOrderValue: RepositoryChangeOrderValue) {
    suspend operator fun invoke(userId: String, order: Order) =
        repositoryChangeOrderValue.changeOrderValue(userId, order)
}
