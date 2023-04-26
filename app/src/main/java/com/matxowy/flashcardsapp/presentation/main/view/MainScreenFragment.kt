package com.matxowy.flashcardsapp.presentation.main.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.matxowy.flashcardsapp.R
import com.matxowy.flashcardsapp.app.utils.extensions.observeWithLifecycle
import com.matxowy.flashcardsapp.data.db.entity.CategoryDetail
import com.matxowy.flashcardsapp.databinding.MainScreenFragmentBinding
import com.matxowy.flashcardsapp.presentation.main.adapters.CategoryAdapter
import com.matxowy.flashcardsapp.presentation.main.viewmodel.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment(R.layout.main_screen_fragment), CategoryAdapter.OnCategoryItemClickListener {

    private val viewModel: MainScreenViewModel by viewModels()
    private var _binding: MainScreenFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = MainScreenFragmentBinding.bind(view)

        val categoryAdapter = CategoryAdapter(this)

        setAdapter(categoryAdapter)
        handleMainScreenEvents()
        setListeners()
        handleViewState(categoryAdapter)
    }

    private fun setAdapter(categoryAdapter: CategoryAdapter) {
        binding.rvCategories.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun handleViewState(categoryAdapter: CategoryAdapter) {
        viewModel.viewState.observeWithLifecycle(viewLifecycleOwner) { viewState ->
            categoryAdapter.submitList(viewState.categories.takeIf { it.isNotEmpty() })
            binding.apply {
                cpiLoading.isVisible = viewState.isLoading
                rvCategories.isVisible = viewState.isLoading.not()
            }
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

    override fun onCategoryItemClick(category: CategoryDetail) {
        viewModel.onCategoryItemClick(category.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
