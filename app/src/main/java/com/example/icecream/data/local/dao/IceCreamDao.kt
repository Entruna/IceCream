package com.example.icecream.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.icecream.data.local.entity.IceCreamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IceCreamDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIceCreams(iceCreams: List<IceCreamEntity>)

    @Query("SELECT * FROM ice_creams")
    fun getAllIceCreamsFlow(): Flow<List<IceCreamEntity>>

    @Query("DELETE FROM ice_creams")
    suspend fun deleteIceCreams()
}