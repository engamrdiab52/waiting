package com.amrabdelhamiddiab.core.data

interface IPreferenceHelper {
    //SERVICE

    //USER logged in
    fun setUserServiceLoggedIn(loggedIn: Boolean)
    fun getUserServiceLoggedIn(): Boolean
    //save Service For Service
    fun saveServiceForService(serviceString: String)
    fun loadServiceForService(): String
    //USER ID
    fun saveUserIdForService(userId: String)
    fun fetchUserIdForService(): String
    //ORDER
    fun saveOrderForService(orderString: String)
    fun loadOrderForService(): String

    //CLIENT

    //SAVE service for client
    fun saveServiceForClient(serviceString: String)
    fun loadServiceForClient(): String
    //ORDER
    fun saveOrderForClient(orderString: String)
    fun loadOrderForClient(): String
    //USER ID for downloading
    fun saveUserIdForClient(userId: String)
    fun fetchUserIdForClient(): String
    //client number
    fun saveClientNumberInPreferences(myNumber: Int)
    fun loadClientNumberFromPreferences(): Int

    fun clearPrefs()
}