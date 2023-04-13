package com.matxowy.flashcardsapp.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.matxowy.flashcardsapp.data.db.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category ORDER BY id")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT name FROM category")
    fun getCategoryNames(): List<String>

    @Query("SELECT name FROM category WHERE id = :categoryId")
    fun getCategoryName(categoryId: Int): String

    @Query("UPDATE category SET amountOfFlashcards = amountOfFlashcards + 1 WHERE id = :categoryId")
    fun incrementFlashcardCount(categoryId: Int)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(category: Category): Long

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)
}
