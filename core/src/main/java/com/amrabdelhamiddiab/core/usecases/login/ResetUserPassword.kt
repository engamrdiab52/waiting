package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryResetUserPassword

class ResetUserPassword(private val repositoryResetUserPassword: RepositoryResetUserPassword) {
    suspend operator fun invoke(email: String) =
        repositoryResetUserPassword.resetUserPassword(email)
}