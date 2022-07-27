package com.amrabdelhamiddiab.core.data.login

interface ISignUpUser {
    suspend fun signupUser(email: String, password: String):Boolean
  //  suspend fun userLoggedIn(): Boolean
}