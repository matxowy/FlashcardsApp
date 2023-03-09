package com.matxowy.flashcardsapp.data.main.repository

import com.matxowy.flashcardsapp.data.db.entity.Category
import kotlinx.coroutines.flow.Flow

interface MainScreenRepository {
    fun getCategories(): Flow<List<Category>>
}
