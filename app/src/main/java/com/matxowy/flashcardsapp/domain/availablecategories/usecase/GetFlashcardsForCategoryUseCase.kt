package com.matxowy.flashcardsapp.domain.availablecategories.usecase

import com.matxowy.flashcardsapp.data.firestoredb.repository.FirestoreRepository
import javax.inject.Inject

class GetFlashcardsForCategoryUseCase @Inject constructor(private val repository: FirestoreRepository) {
    suspend operator fun invoke(language: String, categoryName: String) = repository.getFlashcardsForCategory(language, categoryName)
}
