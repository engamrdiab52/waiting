package com.amrabdelhamiddiab.core.data.login

interface IEmailVerifiedState {
    suspend fun isEmailVerified(): Boolean
}