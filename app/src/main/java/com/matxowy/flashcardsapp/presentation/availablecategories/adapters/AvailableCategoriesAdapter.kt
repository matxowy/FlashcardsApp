package com.matxowy.flashcardsapp.presentation.availablecategories.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.databinding.AvailableCategoryItemBinding

class AvailableCategoriesAdapter(private val listener: OnDownloadCategoryClickListener) :
    ListAdapter<Category, AvailableCategoriesAdapter.AvailableCategoriesViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableCategoriesViewHolder {
        val binding = AvailableCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AvailableCategoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AvailableCategoriesViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class AvailableCategoriesViewHolder(private val binding: AvailableCategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.apply {
                mtvCategoryName.text = category.name
                cbDownload.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        listener.onDownloadCategoryClick(category)
                        cbDownload.isEnabled = false
                    }
                }
            }
        }
    }

    interface OnDownloadCategoryClickListener {
        fun onCategoryItemClick(category: Category)
        fun onDownloadCategoryClick(category: Category)
    }

    class DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Category, newItem: Category) =
            oldItem == newItem
    }
}
