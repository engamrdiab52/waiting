package com.amrabdelhamiddiab.core.domain

data class User(
    val id_user: String,
    val email_user: String,
    val password_user: String,
    val loggedIn_user: Boolean,
    val service_user: Boolean,
    val service: Service
)