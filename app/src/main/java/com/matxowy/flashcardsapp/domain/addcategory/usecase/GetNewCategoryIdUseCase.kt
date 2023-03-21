package com.matxowy.flashcardsapp.domain.addcategory.usecase

import com.matxowy.flashcardsapp.data.db.repository.DatabaseRepository
import javax.inject.Inject

class GetNewCategoryIdUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke() = databaseRepository.getNewCategoryId()
}
