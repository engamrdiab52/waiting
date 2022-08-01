package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryDeleteAccount

class DeleteAccount(private val repositoryDeleteAccount: RepositoryDeleteAccount) {
    suspend operator fun invoke() = repositoryDeleteAccount.deleteAccount()
}