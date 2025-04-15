package com.example.icecream.domain.repository

import com.example.icecream.data.local.entity.IceCreamEntity

interface IceCreamRepository {
    suspend fun getIceCreamsFromDb(): List<IceCreamEntity>
    suspend fun fetchAndStoreIceCreams()
    suspend fun getBasePrice(): Double
}