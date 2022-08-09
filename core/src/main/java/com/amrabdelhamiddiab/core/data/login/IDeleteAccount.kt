package com.amrabdelhamiddiab.core.data.login

interface IDeleteAccount {
    suspend fun deleteAccount(password: String) : Boolean
}