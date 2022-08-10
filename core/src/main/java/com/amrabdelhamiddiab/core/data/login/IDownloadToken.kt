package com.amrabdelhamiddiab.core.data.login

import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.core.domain.Token

interface IDownloadToken {
    suspend fun downloadToken(): Token?
}