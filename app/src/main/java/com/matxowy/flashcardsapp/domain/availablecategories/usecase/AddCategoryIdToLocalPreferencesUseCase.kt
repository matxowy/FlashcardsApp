package com.matxowy.flashcardsapp.domain.availablecategories.usecase

import com.matxowy.flashcardsapp.data.localpreferences.LocalPreferencesRepository
import javax.inject.Inject

class AddCategoryIdToLocalPreferencesUseCase @Inject constructor(private val localPreferencesRepository: LocalPreferencesRepository) {
    operator fun invoke(categoryId: String) = localPreferencesRepository.saveDownloadedCategoryId(categoryId)
}
