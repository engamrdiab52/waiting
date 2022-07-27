package com.amrabdelhamiddiab.core.data.login

class RepositorySendEmailVerification(private val iSendEmailVerification: ISendEmailVerification) {
    suspend fun sendEmailVerificationByFireBae() =
        iSendEmailVerification.sendEmailVerification()

}