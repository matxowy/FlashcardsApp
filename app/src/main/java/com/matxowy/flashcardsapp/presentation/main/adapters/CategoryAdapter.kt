package com.matxowy.flashcardsapp.presentation.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.matxowy.flashcardsapp.data.db.entity.CategoryDetail
import com.matxowy.flashcardsapp.databinding.CategoryItemBinding

class CategoryAdapter(private val listener: OnCategoryItemClickListener) : ListAdapter<CategoryDetail, CategoryAdapter.CategoryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class CategoryViewHolder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val category = getItem(position)
                        listener.onCategoryItemClick(category)
                    }
                }
            }
        }

        fun bind(category: CategoryDetail) {
            binding.apply {
                mtvCategoryName.text = category.name
                mtvAmountOfFlashcards.text = category.amountOfFlashcards.toString()
            }
        }
    }

    interface OnCategoryItemClickListener {
        fun onCategoryItemClick(category: CategoryDetail)
    }

    class DiffCallback : DiffUtil.ItemCallback<CategoryDetail>() {
        override fun areItemsTheSame(oldItem: CategoryDetail, newItem: CategoryDetail) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CategoryDetail, newItem: CategoryDetail) =
            oldItem == newItem
    }
}
