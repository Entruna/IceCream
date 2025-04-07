package com.example.icecream.data.repository

import android.util.Log
import com.example.icecream.common.model.Status
import com.example.icecream.data.local.dao.IceCreamDao
import com.example.icecream.data.local.entity.IceCreamEntity
import com.example.icecream.data.remote.IceCreamApi
import com.example.icecream.domain.repository.IceCreamRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IceCreamRepositoryImpl @Inject constructor(
    private val iceCreamDao: IceCreamDao,
    private val iceCreamApi: IceCreamApi
) : IceCreamRepository {

    override suspend fun getIceCreamsFromDb(status: Status): List<IceCreamEntity> {
        return iceCreamDao.getIceCreamsByStatus(status)
    }

    override suspend fun fetchAndStoreIceCreams() {
        try {

            val response = withContext(Dispatchers.IO) {
                iceCreamApi.getIceCreams()
            }
            val iceCreams = response.iceCreams.map { iceCream ->
                IceCreamEntity(
                    id = iceCream.id,
                    name = iceCream.name,
                    status = Status.valueOf(iceCream.status.uppercase()),
                    imageUrl = iceCream.imageUrl
                )
            }
            withContext(Dispatchers.IO) {
                iceCreamDao.insertIceCreams(iceCreams)

            }
        } catch (e: Exception) {
            Log.e("IceCreamRepository", "Error fetching ice creams", e)
        }
    }

}