package com.amrabdelhamiddiab.core.data

interface IPreferenceHelper {

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
    //in an visit
    fun setIfClientInAVisit(inAVisit: Boolean)
    fun getIfClientInAVisit() : Boolean

    fun getReviewViewed(): Boolean
    fun setReviewViewed(reviewStatus: Boolean)

    fun saveSignInMethode(signInMethode: String)
    fun loadSignInMethode(): String

    // client token id
    fun saveClientTokenId(tokenId:String)
    fun retrieveClientTokenId(): String
    fun clearPrefs()
}