package com.example.icecream.data.repository

import android.util.Log
import com.example.icecream.data.local.dao.BasePriceDao
import com.example.icecream.data.local.dao.IceCreamDao
import com.example.icecream.data.local.entity.IceCreamEntity
import com.example.icecream.data.mapper.IceCreamMapper
import com.example.icecream.data.remote.IceCreamApi
import com.example.icecream.domain.repository.IceCreamRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IceCreamRepositoryImpl @Inject constructor(
    private val iceCreamDao: IceCreamDao,
    private val basePriceDao: BasePriceDao,
    private val iceCreamApi: IceCreamApi,
    private val iceCreamMapper: IceCreamMapper
) : IceCreamRepository {

    override suspend fun getIceCreamsFromDb(): List<IceCreamEntity> {
        return iceCreamDao.getAllIceCreams()
    }

    override suspend fun fetchAndStoreIceCreams() {
        try {

            val response = iceCreamApi.getIceCreams()

            val iceCreams = iceCreamMapper.mapToEntityList(response.iceCreams)

            val basePrice = iceCreamMapper.mapToBasePriceEntity(response.basePrice)



            iceCreamDao.insertIceCreams(iceCreams)
            basePriceDao.insertBasePrice(basePrice)

        } catch (e: Exception) {
            Log.e("IceCreamRepository", "Error fetching ice creams", e)
        }
    }

    override suspend fun getBasePrice(): Double {
        return basePriceDao.getBasePrice()
    }

}