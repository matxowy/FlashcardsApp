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
import kotlinx.coroutines.flow.receiveAsFlow
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

    fun onAddButtonClick(categoryName: String, flashcardFront: String, flashcardBack: String) = viewModelScope.launch(coroutineDispatcher) {
        try {
            val idOfInsertedCategory = insertCategoryUseCase(categoryName).toInt()
            insertFirstFlashcardUseCase(flashcardFront, flashcardBack, idOfInsertedCategory)
            AddFirstFlashcardDialogEvent.SetFragmentResult(RESULT_OK).send()
        } catch (e: Exception) {
            AddFirstFlashcardDialogEvent.SetFragmentResult(RESULT_ERROR).send()
        }
    }

    sealed class AddFirstFlashcardDialogEvent {
        data class SetFragmentResult(val result: Int) : AddFirstFlashcardDialogEvent()
    }

    private fun AddFirstFlashcardDialogEvent.send() {
        viewModelScope.launch { addFirstFlashcardChannel.send(this@send) }
    }
}
