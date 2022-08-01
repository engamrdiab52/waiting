package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryDeleteCurrentOrder

class DeleteCurrentOrder(private val repositoryDeleteCurrentOrder: RepositoryDeleteCurrentOrder) {
    suspend operator fun invoke(userId: String) = repositoryDeleteCurrentOrder.deleteCurrentOrder(userId)
}