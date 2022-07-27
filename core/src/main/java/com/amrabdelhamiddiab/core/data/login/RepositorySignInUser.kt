package com.amrabdelhamiddiab.core.data.login

class RepositorySignInUser(private val iSignInUser: ISignInUser) {
    suspend fun signInUserByFirebase(email: String, password: String) =
        iSignInUser.signInUser(email, password)

}