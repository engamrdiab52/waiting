package com.amrabdelhamiddiab.core.data.login


interface ISignInUser {
    suspend fun signInUser(email: String, password: String):Boolean
}