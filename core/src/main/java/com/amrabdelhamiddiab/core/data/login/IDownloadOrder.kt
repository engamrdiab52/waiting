package com.amrabdelhamiddiab.core.data.login

import com.amrabdelhamiddiab.core.domain.Order

interface IDownloadOrder {
    suspend fun downloadOrder(userId: String): Order?
}