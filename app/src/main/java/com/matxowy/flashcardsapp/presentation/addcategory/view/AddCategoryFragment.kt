package com.matxowy.flashcardsapp.presentation.addcategory.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.matxowy.flashcardsapp.R
import com.matxowy.flashcardsapp.app.utils.extensions.observeWithLifecycle
import com.matxowy.flashcardsapp.databinding.AddCategoryFragmentBinding
import com.matxowy.flashcardsapp.presentation.addcategory.view.AddFirstFlashcardDialogFragment.Companion.ADD_FIRST_FLASHCARD_REQUEST
import com.matxowy.flashcardsapp.presentation.addcategory.view.AddFirstFlashcardDialogFragment.Companion.ADD_FIRST_FLASHCARD_RESULT
import com.matxowy.flashcardsapp.presentation.addcategory.viewmodel.AddCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCategoryFragment : Fragment(R.layout.add_category_fragment) {

    private val viewModel: AddCategoryViewModel by viewModels()
    private var _binding: AddCategoryFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = AddCategoryFragmentBinding.bind(view)

        setOnClickListeners()
        handleAddCategoryEvents()
        setFragmentResultListeners()
    }

    private fun setFragmentResultListeners() {
        setFragmentResultListener(ADD_FIRST_FLASHCARD_REQUEST) { _, bundle ->
            val result = bundle.getInt(ADD_FIRST_FLASHCARD_RESULT)
            viewModel.onAddCategoryResult(result)
        }
    }

    private fun handleAddCategoryEvents() {
        viewModel.addCategoryEvent.observeWithLifecycle(viewLifecycleOwner) { event ->
            when (event) {
                is AddCategoryViewModel.AddCategoryEvent.ShowDialogForAddFlashcard -> {
                    val dialog = AddFirstFlashcardDialogFragment(event.categoryName)
                    dialog.show(parentFragmentManager, ADD_FIRST_FLASHCARD_DIALOG_TAG)
                }
                is AddCategoryViewModel.AddCategoryEvent.ShowDefaultError -> {
                    Snackbar.make(requireView(), getString(R.string.default_error_message), Snackbar.LENGTH_LONG).show()
                }
                is AddCategoryViewModel.AddCategoryEvent.ShowAddCategoryConfirmationMessage -> {
                    Snackbar.make(requireView(), getString(R.string.category_added_message), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.mbAddCategory.setOnClickListener {
            viewModel.onAddCategoryClick(binding.categoryName.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ADD_FIRST_FLASHCARD_DIALOG_TAG = "addFirstFlashcardDialog"
    }
}
