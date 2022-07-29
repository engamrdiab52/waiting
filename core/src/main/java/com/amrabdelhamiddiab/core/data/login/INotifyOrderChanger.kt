package com.amrabdelhamiddiab.core.data.login

interface INotifyOrderChanger {
    suspend fun notifyWhenOrderChanged(): Int?
}