package com.amrabdelhamiddiab.core.data.login

import com.amrabdelhamiddiab.core.domain.Token


interface IListDownloadTokens {
    suspend fun listDownloadTokens() : List<Token>?
}