package com.amrabdelhamiddiab.core.data

interface IPreferenceHelper {
    fun setUserLoggedIn(loggedIn: Boolean)
    fun getUserLoggedIn(): Boolean
    fun clearPrefs()
    fun saveService(serviceString: String)
    fun loadService(): String
    fun saveUserId(userId: String)
    fun fetchUserId(): String
    fun saveOrderClient(orderString: String)
    fun loadOrderClient(): String
}