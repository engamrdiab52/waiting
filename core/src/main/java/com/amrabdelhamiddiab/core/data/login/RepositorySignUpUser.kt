package com.amrabdelhamiddiab.core.data.login

class RepositorySignUpUser(private val iSignupUser: ISignUpUser) {
    suspend fun signUpUserByFirebase(email: String, password: String) =
        iSignupUser.signupUser(email, password)
}