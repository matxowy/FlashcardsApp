package com.matxowy.flashcardsapp.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.matxowy.flashcardsapp.data.db.entity.Flashcard

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcard WHERE id = :flashcardId")
    fun getFlashcardById(flashcardId: Int): Flashcard

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flashcard: Flashcard)

    @Update
    suspend fun update(flashcard: Flashcard)

    @Delete
    suspend fun delete(flashcard: Flashcard)
}
