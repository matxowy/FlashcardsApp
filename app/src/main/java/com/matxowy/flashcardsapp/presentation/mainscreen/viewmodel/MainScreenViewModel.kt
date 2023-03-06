package com.matxowy.flashcardsapp.presentation.mainscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {

    private val mainScreenChannel = Channel<MainScreenEvent>(capacity = Channel.BUFFERED)
    val mainScreenEvent = mainScreenChannel.receiveAsFlow()

    fun onAddCategoryButtonClick() = MainScreenEvent.NavigateToAddCategory.send()

    sealed class MainScreenEvent {
        object NavigateToAddCategory : MainScreenEvent()
    }

    private fun MainScreenEvent.send() {
        viewModelScope.launch { mainScreenChannel.send(this@send) }
    }
}
