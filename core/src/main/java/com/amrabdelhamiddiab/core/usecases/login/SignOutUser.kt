package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositorySignOutUser


class SignOutUser(private val repositorySignOutUser: RepositorySignOutUser) {
    suspend operator fun invoke() = repositorySignOutUser.signOutUserByFirebase()
}