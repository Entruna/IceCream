package com.example.icecream.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.icecream.data.local.entity.BasePriceEntity

@Dao
interface BasePriceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBasePrice(basePriceEntity: BasePriceEntity)

    @Query("SELECT price FROM base_price WHERE id = 0")
    suspend fun getBasePrice(): Double

    @Query("DELETE FROM base_price")
    suspend fun deleteBasePrice()

}