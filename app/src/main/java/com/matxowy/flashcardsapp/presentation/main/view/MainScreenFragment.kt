package com.matxowy.flashcardsapp.presentation.main.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.matxowy.flashcardsapp.R
import com.matxowy.flashcardsapp.app.utils.extensions.observeWithLifecycle
import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.databinding.MainScreenFragmentBinding
import com.matxowy.flashcardsapp.presentation.main.viewmodel.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment(R.layout.main_screen_fragment) {

    private val viewModel: MainScreenViewModel by viewModels()
    private var _binding: MainScreenFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = MainScreenFragmentBinding.bind(view)

        handleMainScreenEvents()
        setListeners()
        handleViewState()
    }

    private fun handleViewState() {
        viewModel.viewState.observeWithLifecycle(viewLifecycleOwner) { viewState ->
            setSpinner(requireContext(), viewState.categories)
            binding.apply {
                cpiLoading.isVisible = viewState.isLoading
                tilSpinnerCategories.isVisible = viewState.isLoading.not()
            }
        }
    }

    private fun setSpinner(context: Context, listOfCategories: List<Category>) {
        val spinnerAdapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, listOfCategories)
        binding.apply {
            spinnerCategories.setAdapter(spinnerAdapter)
            spinnerCategories.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                viewModel.onItemSpinnerClick(listOfCategories[position].id)
                resetSpinnerChoose()
            }
        }
    }

    private fun resetSpinnerChoose() {
        binding.apply {
            spinnerCategories.text.clear()
            spinnerCategories.clearFocus()
        }
    }

    private fun setListeners() {
        binding.apply {
            btnAddCategory.setOnClickListener {
                viewModel.onAddCategoryButtonClick()
            }
            btnAddFlashcards.setOnClickListener {
                viewModel.onAddFlashcardsButtonClick()
            }
        }
    }

    private fun handleMainScreenEvents() {
        viewModel.mainScreenEvent.observeWithLifecycle(viewLifecycleOwner) { event ->
            when (event) {
                is MainScreenViewModel.MainScreenEvent.NavigateToAddCategory -> {
                    val action = MainScreenFragmentDirections.actionMainScreenFragmentToAddCategoryFragment()
                    findNavController().navigate(action)
                }
                is MainScreenViewModel.MainScreenEvent.NavigateToAddFlashcards -> {
                    val action = MainScreenFragmentDirections.actionMainScreenFragmentToAddFlashcardsFragment()
                    findNavController().navigate(action)
                }
                is MainScreenViewModel.MainScreenEvent.NavigateToLearning -> {
                    val action = MainScreenFragmentDirections.actionMainScreenFragmentToLearningScreenFragment(event.categoryId)
                    findNavController().navigate(action)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
