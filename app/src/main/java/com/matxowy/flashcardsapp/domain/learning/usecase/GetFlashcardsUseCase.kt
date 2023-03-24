package com.matxowy.flashcardsapp.domain.learning.usecase

import com.matxowy.flashcardsapp.data.db.repository.DatabaseRepository
import javax.inject.Inject

class GetFlashcardsUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(categoryId: Int) = databaseRepository.getFlashcardsForCategory(categoryId)
}
