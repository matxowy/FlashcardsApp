package com.matxowy.flashcardsapp.presentation.mainscreen.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.matxowy.flashcardsapp.R
import com.matxowy.flashcardsapp.presentation.mainscreen.viewmodel.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment(R.layout.main_screen_fragment) {

    private val viewModel: MainScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
