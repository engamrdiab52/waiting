package com.amrabdelhamiddiab.core.data.login

import com.amrabdelhamiddiab.core.domain.Order

class RepositoryChangeOrderValue(private val iChangeOrderValue: IChangeOrderValue) {
    suspend fun changeOrderValue(userId: String, order: Order) =
        iChangeOrderValue.changeOrderValue(userId, order)
}