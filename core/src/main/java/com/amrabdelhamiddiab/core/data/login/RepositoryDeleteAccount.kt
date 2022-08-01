package com.amrabdelhamiddiab.core.data.login

class RepositoryDeleteAccount(private val iDeleteAccount: IDeleteAccount) {
    suspend fun deleteAccount() = iDeleteAccount.deleteAccount()
}