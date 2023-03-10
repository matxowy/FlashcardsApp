package com.matxowy.flashcardsapp.data.learning.repository

import com.matxowy.flashcardsapp.data.db.dao.CategoryDao
import com.matxowy.flashcardsapp.data.db.dao.FlashcardDao
import com.matxowy.flashcardsapp.data.db.entity.Flashcard
import javax.inject.Inject

class LearningScreenRemoteRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val flashcardDao: FlashcardDao
) : LearningScreenRepository {
    override suspend fun getCategoryName(categoryId: Int) = categoryDao.getCategoryName(categoryId)

    override suspend fun getFlashcardsForCategory(categoryId: Int): List<Flashcard> = flashcardDao.getFlashcardsForCategory(categoryId)
}
