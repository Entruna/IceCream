package com.example.icecream.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.icecream.data.local.entity.BasePriceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BasePriceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBasePrice(basePriceEntity: BasePriceEntity)

    @Query("SELECT price FROM base_price WHERE id = 0")
    fun getBasePriceFlow(): Flow<Double>

    @Query("DELETE FROM base_price")
    suspend fun deleteBasePrice()

}