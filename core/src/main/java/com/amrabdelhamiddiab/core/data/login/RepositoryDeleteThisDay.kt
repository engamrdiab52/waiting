package com.amrabdelhamiddiab.core.data.login

class RepositoryDeleteThisDay (private val iDeleteThisDay: IDeleteThisDay) {
    suspend fun deleteThisDay() = iDeleteThisDay.deleteThisDay()
}