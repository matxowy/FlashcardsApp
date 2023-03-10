package com.matxowy.flashcardsapp.data.learning.repository

import com.matxowy.flashcardsapp.data.db.entity.Flashcard

interface LearningScreenRepository {
    suspend fun getCategoryName(categoryId: Int): String
    suspend fun getFlashcardsForCategory(categoryId: Int): List<Flashcard>
}
