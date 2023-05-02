package com.matxowy.flashcardsapp.presentation.availablecategories.viewmodel

import android.os.LocaleList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.domain.availablecategories.usecase.AddCategoryIdToLocalPreferencesUseCase
import com.matxowy.flashcardsapp.domain.availablecategories.usecase.AddCategoryToLocalDatabaseUseCase
import com.matxowy.flashcardsapp.domain.availablecategories.usecase.AddFlashcardForCategoryUseCase
import com.matxowy.flashcardsapp.domain.availablecategories.usecase.GetAvailableCategoriesUseCase
import com.matxowy.flashcardsapp.domain.availablecategories.usecase.GetFlashcardsForCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AvailableCategoriesViewModel @Inject constructor(
    private val getAvailableCategoriesUseCase: GetAvailableCategoriesUseCase,
    private val getFlashcardsForCategoryUseCase: GetFlashcardsForCategoryUseCase,
    private val addCategoryToLocalDatabaseUseCase: AddCategoryToLocalDatabaseUseCase,
    private val addCategoryIdToLocalPreferencesUseCase: AddCategoryIdToLocalPreferencesUseCase,
    private val addFlashcardForCategoryUseCase: AddFlashcardForCategoryUseCase,
    @Named("IO") private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val availableCategoriesChannel = Channel<AvailableCategoriesEvent>(capacity = Channel.BUFFERED)
    val availableCategoriesEvent = availableCategoriesChannel.receiveAsFlow()

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    private val currentLanguage = LocaleList.getDefault().get(0).language
    init {
        viewModelScope.launch(coroutineDispatcher) {
            val availableCategories = getAvailableCategoriesUseCase(currentLanguage)
            _viewState.update { it.copy(isLoading = false, categories = availableCategories, isListEmpty = availableCategories.isEmpty()) }
        }
    }

    fun onDownloadCategoryClick(category: Category) = viewModelScope.launch(coroutineDispatcher) {
        try {
            val categoryFlashcards = getFlashcardsForCategoryUseCase(currentLanguage, category.name)
            addCategoryToLocalDatabaseUseCase(category)
            for (flashcard in categoryFlashcards) {
                addFlashcardForCategoryUseCase(flashcard)
            }
            addCategoryIdToLocalPreferencesUseCase(category.id.toString())
            AvailableCategoriesEvent.ShowDownloadConfirmationMessage.send()
        } catch (e: Exception) {
            AvailableCategoriesEvent.ShowDefaultErrorMessage.send()
        }
    }

    data class ViewState(
        val isLoading: Boolean = true,
        val categories: List<Category> = emptyList(),
        val isListEmpty: Boolean = false
    )

    sealed class AvailableCategoriesEvent {
        object ShowDownloadConfirmationMessage : AvailableCategoriesEvent()
        object ShowDefaultErrorMessage : AvailableCategoriesEvent()
    }

    private fun AvailableCategoriesEvent.send() {
        viewModelScope.launch { availableCategoriesChannel.send(this@send) }
    }
}
