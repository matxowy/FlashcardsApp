package com.matxowy.flashcardsapp.data.localpreferences

import android.content.SharedPreferences
import javax.inject.Inject

class LocalPreferencesRemoteRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : LocalPreferencesRepository {

    companion object {
        const val USER_NOTIFICATION_TOKEN = "userNotificationToken"
    }

    override fun getUserNotificationToken(): String? = sharedPreferences.getString(USER_NOTIFICATION_TOKEN, "")

    override fun saveUserNotificationToken(token: String) {
        sharedPreferences.edit().putString(USER_NOTIFICATION_TOKEN, token).apply()
    }
}
