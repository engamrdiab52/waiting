package com.amrabdelhamiddiab.core.usecases.login

import com.amrabdelhamiddiab.core.data.login.RepositoryNotifyOrderChanger

class NotifyOrderChanger(private val repositoryNotifyOrderChanger: RepositoryNotifyOrderChanger) {
    suspend operator fun invoke() = repositoryNotifyOrderChanger.notifyWhenOrderChanged()
}