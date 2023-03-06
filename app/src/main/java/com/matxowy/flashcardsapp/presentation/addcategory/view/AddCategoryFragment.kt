package com.matxowy.flashcardsapp.presentation.addcategory.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.matxowy.flashcardsapp.R
import com.matxowy.flashcardsapp.presentation.addcategory.viewmodel.AddCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCategoryFragment : Fragment(R.layout.add_category_fragment) {

    private val viewModel: AddCategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
