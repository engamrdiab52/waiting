package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryEmailVerifiedState


class EmailVerifiedState(private val repositoryEmailVerifiedState: RepositoryEmailVerifiedState) {
    suspend operator fun invoke() = repositoryEmailVerifiedState.isEmailVerified()

}