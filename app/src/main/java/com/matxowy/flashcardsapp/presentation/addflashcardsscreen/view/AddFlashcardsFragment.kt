package com.matxowy.flashcardsapp.presentation.addflashcardsscreen.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.matxowy.flashcardsapp.R
import com.matxowy.flashcardsapp.presentation.addflashcardsscreen.viewmodel.AddFlashcardsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFlashcardsFragment : Fragment(R.layout.add_flashcards_fragment) {
    private val viewModel: AddFlashcardsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
