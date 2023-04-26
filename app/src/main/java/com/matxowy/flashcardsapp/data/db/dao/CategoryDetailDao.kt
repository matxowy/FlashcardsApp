package com.matxowy.flashcardsapp.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.matxowy.flashcardsapp.data.db.entity.CategoryDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDetailDao {
    @Query("SELECT * FROM CategoryDetail ORDER BY id")
    fun getCategoriesWithDetails(): Flow<List<CategoryDetail>>
}
