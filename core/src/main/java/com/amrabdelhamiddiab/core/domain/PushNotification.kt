package com.amrabdelhamiddiab.core.domain

data class PushNotification(
    val data: NotificationData,
    val to: String
)