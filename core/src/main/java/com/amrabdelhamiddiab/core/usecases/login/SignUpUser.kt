package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositorySignUpUser


class SignUpUser(private val repositorySignUpUser: RepositorySignUpUser) {
    suspend operator fun invoke(email: String, password: String) =
        repositorySignUpUser.signUpUserByFirebase(email, password)
}