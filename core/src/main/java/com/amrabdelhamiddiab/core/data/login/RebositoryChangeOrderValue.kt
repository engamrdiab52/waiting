package com.amrabdelhamiddiab.core.data.login

class RepositoryChangeOrderValue(private val iChangeOrderValue: IChangeOrderValue) {
    suspend fun changeOrderValue(value: Int) = iChangeOrderValue.changeOrderValue(value)
}