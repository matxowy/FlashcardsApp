package com.matxowy.flashcardsapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val amountOfFlashcards: Int = 1
) {
    override fun toString(): String = this.name
}
