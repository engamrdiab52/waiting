package com.amrabdelhamiddiab.core.data.login

class RepositoryDownloadOrder(private val iDownloadOrder: IDownloadOrder) {
    suspend fun downloadOrder(userId: String) = iDownloadOrder.downloadOrder(userId)
}