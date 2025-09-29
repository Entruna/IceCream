package com.example.icecream.domain.repository

import com.example.icecream.data.local.entity.IceCreamEntity
import kotlinx.coroutines.flow.Flow

interface IceCreamRepository {
    fun getIceCreamsFromDbFlow(): Flow<List<IceCreamEntity>>
    suspend fun fetchAndStoreIceCreams()
    fun getBasePriceFlow(): Flow<Double>
}