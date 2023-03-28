package com.matxowy.flashcardsapp.presentation.addcategory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matxowy.flashcardsapp.app.utils.constants.ResultCodes.RESULT_ERROR
import com.matxowy.flashcardsapp.app.utils.constants.ResultCodes.RESULT_OK
import com.matxowy.flashcardsapp.domain.addcategory.usecase.InsertCategoryUseCase
import com.matxowy.flashcardsapp.domain.addcategory.usecase.InsertFirstFlashcardUseCase
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
class AddFirstFlashcardDialogViewModel @Inject constructor(
    private val insertCategoryUseCase: InsertCategoryUseCase,
    private val insertFirstFlashcardUseCase: InsertFirstFlashcardUseCase,
    @Named("IO") private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val addFirstFlashcardChannel = Channel<AddFirstFlashcardDialogEvent>(capacity = Channel.BUFFERED)
    val addFirstFlashcardEvent = addFirstFlashcardChannel.receiveAsFlow()

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    fun onAddButtonClick(categoryName: String, flashcardFront: String, flashcardBack: String) = viewModelScope.launch(coroutineDispatcher) {
        try {
            val idOfInsertedCategory = insertCategoryUseCase(categoryName).toInt()
            insertFirstFlashcardUseCase(flashcardFront, flashcardBack, idOfInsertedCategory)
            AddFirstFlashcardDialogEvent.SetFragmentResult(RESULT_OK).send()
        } catch (e: Exception) {
            AddFirstFlashcardDialogEvent.SetFragmentResult(RESULT_ERROR).send()
        }
    }

    fun setButtonState(flashcardFrontText: String, flashcardBackText: String) {
        val isButtonEnable = flashcardFrontText.isNotBlank() && flashcardBackText.isNotBlank()
        _viewState.update { it.copy(isAddButtonEnable = isButtonEnable) }
    }

    data class ViewState(
        val isAddButtonEnable: Boolean = false
    )

    sealed class AddFirstFlashcardDialogEvent {
        data class SetFragmentResult(val result: Int) : AddFirstFlashcardDialogEvent()
    }

    private fun AddFirstFlashcardDialogEvent.send() {
        viewModelScope.launch { addFirstFlashcardChannel.send(this@send) }
    }
}
