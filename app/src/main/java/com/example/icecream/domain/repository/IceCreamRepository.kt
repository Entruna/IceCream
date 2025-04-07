package com.example.icecream.domain.repository

import com.example.icecream.common.model.Status
import com.example.icecream.data.local.entity.IceCreamEntity

interface IceCreamRepository {
    suspend fun getIceCreamsFromDb(status: Status): List<IceCreamEntity>
    suspend fun fetchAndStoreIceCreams()
}