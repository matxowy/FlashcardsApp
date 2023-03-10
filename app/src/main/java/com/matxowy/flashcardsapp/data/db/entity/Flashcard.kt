package com.matxowy.flashcardsapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard")
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val frontText: String,
    val backText: String,
    val categoryId: Int
)
