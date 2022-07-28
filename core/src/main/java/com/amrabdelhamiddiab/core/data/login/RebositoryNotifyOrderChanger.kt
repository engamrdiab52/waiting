package com.amrabdelhamiddiab.core.data.login

class RepositoryNotifyOrderChanger(private val iNotifyOrderChanger: INotifyOrderChanger) {
    suspend fun notifyWhenOrderChanged() = iNotifyOrderChanger.notifyWhenOrderChanged()
}