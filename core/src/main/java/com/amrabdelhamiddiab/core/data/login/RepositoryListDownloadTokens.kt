package com.amrabdelhamiddiab.core.data.login

class RepositoryListDownloadTokens(private val iListDownloadTokens: IListDownloadTokens) {
    suspend fun listDownloadTokens() = iListDownloadTokens.listDownloadTokens()
}