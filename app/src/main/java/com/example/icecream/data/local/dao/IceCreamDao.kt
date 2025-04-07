package com.example.icecream.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.icecream.data.local.entity.IceCreamEntity
import com.example.icecream.common.model.Status

@Dao
interface IceCreamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIceCreams(iceCreams: List<IceCreamEntity>)

    @Query("SELECT * FROM ice_creams WHERE id = :id")
    suspend fun getIceCreamById(id: Long): IceCreamEntity?

    @Query("SELECT * FROM ice_creams")
    suspend fun getAllIceCreams(): List<IceCreamEntity>

    @Query("SELECT * FROM ice_creams WHERE status = :status")
    suspend fun getIceCreamsByStatus(status: Status): List<IceCreamEntity>

}