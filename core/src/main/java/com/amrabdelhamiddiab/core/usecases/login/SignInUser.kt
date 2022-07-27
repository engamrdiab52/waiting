package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositorySignInUser

class SignInUser(private val repositorySignInUser: RepositorySignInUser) {
    suspend operator fun invoke(email: String, password: String) =
        repositorySignInUser.signInUserByFirebase(email, password)
}