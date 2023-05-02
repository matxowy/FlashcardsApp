package com.matxowy.flashcardsapp.presentation.availablecategories.viewmodel

import android.os.LocaleList
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.domain.availablecategories.usecase.AddCategoryToLocalDatabaseUseCase
import com.matxowy.flashcardsapp.domain.availablecategories.usecase.AddFlashcardForCategoryUseCase
import com.matxowy.flashcardsapp.domain.availablecategories.usecase.GetAvailableCategoriesUseCase
import com.matxowy.flashcardsapp.domain.availablecategories.usecase.GetFlashcardsForCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AvailableCategoriesViewModel @Inject constructor(
    private val getAvailableCategoriesUseCase: GetAvailableCategoriesUseCase,
    private val getFlashcardsForCategoryUseCase: GetFlashcardsForCategoryUseCase,
    private val addCategoryToLocalDatabaseUseCase: AddCategoryToLocalDatabaseUseCase,
    private val addFlashcardForCategoryUseCase: AddFlashcardForCategoryUseCase,
    @Named("IO") private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    private val currentLanguage = LocaleList.getDefault().get(0).language

    init {
        viewModelScope.launch(coroutineDispatcher) {
            val availableCategories = getAvailableCategoriesUseCase(currentLanguage)
            _viewState.update { it.copy(isLoading = false, categories = availableCategories) }
        }
    }

    fun onDownloadCategoryClick(category: Category) = viewModelScope.launch(coroutineDispatcher) {
        try {
            val categoryFlashcards = getFlashcardsForCategoryUseCase(currentLanguage, category.name)
            addCategoryToLocalDatabaseUseCase(category)
            for (flashcard in categoryFlashcards) {
                addFlashcardForCategoryUseCase(flashcard)
            }
        } catch (e: Exception) {
            Log.e("categories", e.toString())
        }
    }

    data class ViewState(
        val isLoading: Boolean = true,
        val categories: List<Category> = emptyList()
    )
}
