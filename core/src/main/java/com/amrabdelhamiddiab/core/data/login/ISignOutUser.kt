package com.amrabdelhamiddiab.core.data.login

interface ISignOutUser {
    // may need a return boolean value
    suspend fun signOutUser() :Boolean
}