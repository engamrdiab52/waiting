package com.amrabdelhamiddiab.core.data.login

class RepositorySignOutUser(private val iSignOutUser: ISignOutUser) {
    suspend fun signOutUserByFirebase() = iSignOutUser.signOutUser()
}