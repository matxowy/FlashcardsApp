package com.matxowy.flashcardsapp.data.main.repository

import com.matxowy.flashcardsapp.data.db.dao.CategoryDao
import javax.inject.Inject

class MainScreenRemoteRepository @Inject constructor(
    private val categoryDao: CategoryDao
) : MainScreenRepository {
    override fun getCategories() = categoryDao.getCategories()
}
