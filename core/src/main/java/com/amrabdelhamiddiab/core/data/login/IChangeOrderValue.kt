package com.amrabdelhamiddiab.core.data.login

import com.amrabdelhamiddiab.core.domain.Order

interface IChangeOrderValue {
    suspend fun changeOrderValue(order: Order) :Boolean
}