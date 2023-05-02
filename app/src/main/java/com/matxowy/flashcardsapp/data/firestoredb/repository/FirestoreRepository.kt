package com.matxowy.flashcardsapp.data.firestoredb.repository

import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.data.db.entity.Flashcard

interface FirestoreRepository {
    suspend fun getAvailableCategories(language: String): List<Category>
    suspend fun getFlashcardsForCategory(language: String, categoryName: String): List<Flashcard>
}
