package com.matxowy.flashcardsapp.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.matxowy.flashcardsapp.data.db.entity.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category ORDER BY id")
    fun getCategories(): List<Category>
}
