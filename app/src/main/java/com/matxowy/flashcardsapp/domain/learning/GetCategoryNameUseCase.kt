package com.matxowy.flashcardsapp.domain.learning

import com.matxowy.flashcardsapp.data.learning.repository.LearningScreenRepository
import javax.inject.Inject

class GetCategoryNameUseCase @Inject constructor(
    private val learningScreenRepository: LearningScreenRepository
) {
    suspend operator fun invoke(categoryId: Int) = learningScreenRepository.getCategoryName(categoryId)
}
