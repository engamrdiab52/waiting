package com.amrabdelhamiddiab.core.data.login

class RepositoryDeleteAccount(private val iDeleteAccount: IDeleteAccount) {
    suspend fun deleteAccount(password: String) = iDeleteAccount.deleteAccount(password)
}