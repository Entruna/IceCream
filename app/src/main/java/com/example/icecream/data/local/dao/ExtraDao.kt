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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExtras(extras: List<ExtraEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<ExtraCategoryEntity>): List<Long>

    @Transaction
    @Query("SELECT * FROM extra_categories")
    suspend fun getCategoriesWithExtras(): List<ExtraCategoryWithExtras>

    @Query("SELECT * FROM extra_categories")
    suspend fun getAllCategories(): List<ExtraCategoryEntity>

    @Query("SELECT * FROM extras")
    suspend fun getAllExtras(): List<ExtraEntity>

    @Transaction
    @Query("SELECT * FROM extra_categories WHERE id IN (SELECT categoryId FROM extras WHERE id IN (:extraIds))")
    suspend fun getCategoriesWithExtrasByExtraIds(extraIds: List<Long>): List<ExtraCategoryWithExtras>

    @Query("DELETE FROM extra_categories")
    suspend fun deleteAll()

}