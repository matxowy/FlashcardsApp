package com.matxowy.flashcardsapp.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.matxowy.flashcardsapp.data.db.entity.Flashcard

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcard WHERE id = :flashcardId")
    fun getFlashcardById(flashcardId: Int): Flashcard
}
