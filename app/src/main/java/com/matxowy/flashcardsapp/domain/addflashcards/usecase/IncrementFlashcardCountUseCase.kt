package com.matxowy.flashcardsapp.domain.addflashcards.usecase

import com.matxowy.flashcardsapp.data.db.repository.DatabaseRepository
import javax.inject.Inject

class IncrementFlashcardCountUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) {
    suspend operator fun invoke(categoryId: Int) = databaseRepository.incrementFlashcardCount(categoryId)
}
