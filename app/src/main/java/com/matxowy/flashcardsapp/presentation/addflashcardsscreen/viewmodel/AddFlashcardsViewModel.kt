package com.matxowy.flashcardsapp.presentation.addflashcardsscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.domain.addflashcards.usecase.GetCategoriesUseCase
import com.matxowy.flashcardsapp.domain.addflashcards.usecase.InsertFlashcardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AddFlashcardsViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val insertFlashcardUseCase: InsertFlashcardUseCase,
    @Named("IO") private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val addFlashcardsChannel = Channel<AddFlashcardsEvent>(capacity = Channel.BUFFERED)
    val addFlashcardsEvent = addFlashcardsChannel.receiveAsFlow()

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    private var categoryId = DEFAULT_VALUE

    init {
        viewModelScope.launch(coroutineDispatcher) {
            val categories = getCategoriesUseCase()
            categories.collectLatest { categoriesList ->
                _viewState.update { it.copy(isLoading = false, categories = categoriesList) }
            }
        }
    }

    fun onItemSpinnerClick(categoryId: Int) {
        this.categoryId = categoryId
    }

    fun onAddFlashcardClick(flashcardFrontText: String, flashcardBackText: String) = viewModelScope.launch(coroutineDispatcher) {
        try {
            insertFlashcardUseCase(flashcardFrontText, flashcardBackText, categoryId)
            AddFlashcardsEvent.ShowAddFlashcardConfirmation.send()
        } catch (e: Exception) {
            AddFlashcardsEvent.ShowDefaultError.send()
        }
    }

    data class ViewState(
        val isLoading: Boolean = true,
        val categories: List<Category> = emptyList()
    )

    sealed class AddFlashcardsEvent {
        object ShowDefaultError : AddFlashcardsEvent()
        object ShowAddFlashcardConfirmation : AddFlashcardsEvent()
    }

    private fun AddFlashcardsEvent.send() {
        viewModelScope.launch { addFlashcardsChannel.send(this@send) }
    }

    companion object {
        const val DEFAULT_VALUE = 0
    }
}
