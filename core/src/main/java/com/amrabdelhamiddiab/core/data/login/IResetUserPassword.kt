package com.amrabdelhamiddiab.core.data.login

interface IResetUserPassword {
    suspend fun resetPassword(email: String):Boolean
}