package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositorySendEmailVerification


class SendEmailVerification(private val repositorySendEmailVerification: RepositorySendEmailVerification) {
    suspend operator fun invoke() = repositorySendEmailVerification.sendEmailVerificationByFireBae()
}