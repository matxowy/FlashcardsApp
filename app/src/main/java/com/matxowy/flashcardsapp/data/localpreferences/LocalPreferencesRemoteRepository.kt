package com.matxowy.flashcardsapp.data.localpreferences

import android.content.SharedPreferences
import javax.inject.Inject

class LocalPreferencesRemoteRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : LocalPreferencesRepository {

    private val categoryIdSet = mutableSetOf<String>()

    companion object {
        const val USER_NOTIFICATION_TOKEN = "userNotificationToken"
        const val DOWNLOADED_CATEGORY_ID = "downloadedCategoryId"
    }

    override fun getUserNotificationToken(): String? = sharedPreferences.getString(USER_NOTIFICATION_TOKEN, "")

    override fun saveUserNotificationToken(token: String) {
        sharedPreferences.edit().putString(USER_NOTIFICATION_TOKEN, token).apply()
    }

    override fun getDownloadedCategoryIds(): MutableSet<String>? = sharedPreferences.getStringSet(DOWNLOADED_CATEGORY_ID, setOf())

    override fun saveDownloadedCategoryId(categoryId: String) {
        categoryIdSet.add(categoryId)
        sharedPreferences.edit().putStringSet(DOWNLOADED_CATEGORY_ID, categoryIdSet).apply()
    }
}
