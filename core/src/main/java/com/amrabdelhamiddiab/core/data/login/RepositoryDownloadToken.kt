package com.amrabdelhamiddiab.core.data.login

class RepositoryDownloadToken(private val iDownloadToken: IDownloadToken) {
    suspend fun downloadToken() = iDownloadToken.downloadToken()
}