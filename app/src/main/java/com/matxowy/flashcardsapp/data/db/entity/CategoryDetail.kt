package com.matxowy.flashcardsapp.data.db.entity

import androidx.room.DatabaseView

@DatabaseView(
    "SELECT category.id, category.name, count(*) as amountOfFlashcards FROM category " +
        "JOIN flashcard ON category.id == flashcard.categoryId GROUP BY category.id"
)
data class CategoryDetail(
    val id: Int,
    val name: String,
    val amountOfFlashcards: Int
)
