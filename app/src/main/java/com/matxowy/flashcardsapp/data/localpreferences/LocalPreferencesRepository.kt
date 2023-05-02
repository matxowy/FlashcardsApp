package com.matxowy.flashcardsapp.data.localpreferences

interface LocalPreferencesRepository {
    fun getUserNotificationToken(): String?
    fun saveUserNotificationToken(token: String)
    fun getDownloadedCategoryIds(): MutableSet<String>?
    fun saveDownloadedCategoryId(categoryId: String)
}
