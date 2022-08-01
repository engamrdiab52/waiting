package com.amrabdelhamiddiab.waiting.framework.utilis

import android.content.Context
import android.content.SharedPreferences
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceManager @Inject constructor(@ApplicationContext context: Context) :
    IPreferenceHelper {
    companion object {
        //SERVICE
        const val USER_LOGGED_IN = "userLoggedIn"
        const val USER_SERVICE_FOR_SERVICE = "userServiceForService"
        const val USER_ID_FOR_SERVICE = "userIdForService"
        const val SERVICE_ORDER = "service_order"
        //CLIENT
        const val USER_ID_FOR_CLIENT = "userIdForClient"
        const val CLIENT_ORDER ="orderOfClient"
        const val MY_NUMBER = "myNumber"
        const val SERVICE_FOR_CLIENT = "serviceForClient"
    }

    private val PREFS_NAME = "PHARMACYPreferences"
    private var preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun setUserServiceLoggedIn(loggedIn: Boolean) {
        preferences[USER_LOGGED_IN] = loggedIn
    }

    override fun getUserServiceLoggedIn(): Boolean {
        return preferences[USER_LOGGED_IN] ?: false
    }

    override fun saveServiceForService(serviceString: String) {
        preferences[USER_SERVICE_FOR_SERVICE] = serviceString
    }

    override fun loadServiceForService(): String {
        return preferences[USER_SERVICE_FOR_SERVICE] ?: ""
    }

    override fun saveUserIdForClient(userId: String) {
       preferences[USER_ID_FOR_CLIENT] = userId
    }

    override fun fetchUserIdForClient(): String {
        return preferences[USER_ID_FOR_CLIENT] ?: ""
    }

    override fun saveOrderForClient(orderString: String) {
        preferences[CLIENT_ORDER] = orderString
    }

    override fun loadOrderForClient(): String {
        return preferences[CLIENT_ORDER] ?: ""
    }

    override fun saveUserIdForService(userId: String) {
        preferences[USER_ID_FOR_SERVICE] = userId
    }

    override fun fetchUserIdForService(): String {
        return preferences[USER_ID_FOR_SERVICE] ?: ""
    }

    override fun saveOrderForService(orderString: String) {
        preferences[SERVICE_ORDER] = orderString
    }

    override fun loadOrderForService(): String {
        return preferences[SERVICE_ORDER] ?: ""
    }

    override fun saveServiceForClient(serviceString: String) {
        preferences[SERVICE_FOR_CLIENT] = serviceString
    }

    override fun loadServiceForClient(): String {
        return preferences[SERVICE_FOR_CLIENT] ?: ""
    }

    override fun saveClientNumberInPreferences(myNumber: Int) {
        preferences[MY_NUMBER] = myNumber
    }

    override fun loadClientNumberFromPreferences(): Int {
        return preferences[MY_NUMBER] ?: 0
    }


    override fun clearPrefs() {
        preferences.edit().clear().apply()
    }


    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    private operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    /**
     * finds value on given key.
     * [T] is the type of value
     * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    private inline operator fun <reified T : Any> SharedPreferences.get(
        key: String,
        defaultValue: T? = null
    ): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
}