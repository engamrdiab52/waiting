package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryDownloadOrder

class DownloadOrder(private val repositoryDownloadOrder: RepositoryDownloadOrder) {
    suspend operator fun invoke(userId: String) = repositoryDownloadOrder.downloadOrder(userId)
}