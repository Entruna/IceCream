package com.example.icecream.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.icecream.data.local.entity.ExtraCategoryEntity
import com.example.icecream.data.local.entity.ExtraCategoryWithExtras
import com.example.icecream.data.local.entity.ExtraEntity

@Dao
interface ExtraDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExtras(extras: List<ExtraEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<ExtraCategoryEntity>)

    @Transaction
    suspend fun insertExtrasAndCategories(categories: List<ExtraCategoryEntity>, extras: List<ExtraEntity>) {
        insertCategories(categories)
        insertExtras(extras)
    }

    @Query("SELECT * FROM extras WHERE id = :id")
    suspend fun getExtraById(id: Long): ExtraEntity?

    @Transaction
    @Query("SELECT * FROM extra_categories")
    suspend fun getCategoriesWithExtras(): List<ExtraCategoryWithExtras>

    @Delete
    suspend fun deleteExtra(extra: ExtraEntity)
}