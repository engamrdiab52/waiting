package com.amrabdelhamiddiab.core.data.login

class RepositoryEmailVerifiedState(private val iEmailVerifiedState: IEmailVerifiedState) {
    suspend fun isEmailVerified()= iEmailVerifiedState.isEmailVerified()
}