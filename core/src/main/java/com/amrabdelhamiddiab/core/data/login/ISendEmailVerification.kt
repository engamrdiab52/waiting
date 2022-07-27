package com.amrabdelhamiddiab.core.data.login

interface ISendEmailVerification {
    suspend fun sendEmailVerification():Boolean
}