package com.example.icecream.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.icecream.data.local.entity.ExtraEntity

@Dao
interface ExtraDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExtras(extras: List<ExtraEntity>)

    @Query("SELECT * FROM extras WHERE id = :id")
    suspend fun getExtraById(id: Long): ExtraEntity?

    @Query("SELECT * FROM extras")
    suspend fun getAllExtras(): List<ExtraEntity>

    @Delete
    suspend fun deleteExtra(extra: ExtraEntity)
}