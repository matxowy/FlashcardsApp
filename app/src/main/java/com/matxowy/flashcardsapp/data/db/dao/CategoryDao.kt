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

    @Query("SELECT name FROM category WHERE id = :categoryId")
    fun getCategoryName(categoryId: Int): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)
}
