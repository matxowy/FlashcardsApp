package com.matxowy.flashcardsapp.presentation.availablecategories.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.matxowy.flashcardsapp.R
import com.matxowy.flashcardsapp.app.utils.extensions.observeWithLifecycle
import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.databinding.AvailableCategoriesFragmentBinding
import com.matxowy.flashcardsapp.presentation.availablecategories.adapters.AvailableCategoriesAdapter
import com.matxowy.flashcardsapp.presentation.availablecategories.viewmodel.AvailableCategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvailableCategoriesFragment : Fragment(R.layout.available_categories_fragment), AvailableCategoriesAdapter.OnDownloadCategoryClickListener {

    private val viewModel: AvailableCategoriesViewModel by viewModels()
    private var _binding: AvailableCategoriesFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = AvailableCategoriesFragmentBinding.bind(view)

        val availableCategoriesAdapter = AvailableCategoriesAdapter(this)

        setAdapter(availableCategoriesAdapter)
        handleViewState(availableCategoriesAdapter)
        handleAvailableCategoriesEvents()
    }

    private fun handleAvailableCategoriesEvents() {
        viewModel.availableCategoriesEvent.observeWithLifecycle(viewLifecycleOwner) { event ->
            when (event) {
                AvailableCategoriesViewModel.AvailableCategoriesEvent.ShowDownloadConfirmationMessage -> {
                    Snackbar.make(requireView(), R.string.category_successfully_saved_text, Snackbar.LENGTH_LONG).show()
                }
                AvailableCategoriesViewModel.AvailableCategoriesEvent.ShowDefaultErrorMessage -> {
                    Snackbar.make(requireView(), R.string.default_error_message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setAdapter(availableCategoriesAdapter: AvailableCategoriesAdapter) {
        binding.rvAvailableCategories.apply {
            adapter = availableCategoriesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun handleViewState(availableCategoriesAdapter: AvailableCategoriesAdapter) {
        viewModel.viewState.observeWithLifecycle(viewLifecycleOwner) { viewState ->
            availableCategoriesAdapter.submitList(viewState.categories.takeIf { it.isNotEmpty() })
            binding.apply {
                cpiLoading.isVisible = viewState.isLoading
                rvAvailableCategories.isVisible = viewState.isLoading.not()
                mtvNoNewCategories.isVisible = viewState.isListEmpty
            }
        }
    }

    override fun onDownloadCategoryClick(category: Category) {
        viewModel.onDownloadCategoryClick(category)
    }

    override fun onCategoryItemClick(category: Category) {
        // TODO: [FCA-18] Expand this feature with this functionality. Add dialog after click on item to show description about category.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
