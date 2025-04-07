package com.example.icecream.data.repository

import android.util.Log
import com.example.icecream.data.local.dao.ExtraDao
import com.example.icecream.data.local.entity.ExtraCategoryEntity
import com.example.icecream.data.local.entity.ExtraCategoryWithExtras
import com.example.icecream.data.local.entity.ExtraEntity
import com.example.icecream.data.remote.IceCreamApi
import com.example.icecream.domain.repository.ExtraRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExtraRepositoryImpl @Inject constructor(
    private val extraDao: ExtraDao,
    private val iceCreamApi: IceCreamApi
) : ExtraRepository {


    override suspend fun fetchAndStoreExtras() {
        try {
            val response = withContext(Dispatchers.IO) {
                iceCreamApi.getExtras()
            }

            val categories = response.map { category ->
                ExtraCategoryEntity(
                    type = category.type, required = category.required
                )
            }

            val extras = response.flatMap { category ->
                val categoryId = categories.first { it.type == category.type }.id
                category.items.map { item ->
                    ExtraEntity(
                        id = item.id, name = item.name, price = item.price, categoryId = categoryId
                    )
                }
            }

            withContext(Dispatchers.IO) {
                extraDao.insertExtrasAndCategories(categories, extras)
            }

        } catch (e: Exception) {
            Log.e("IceCreamRepository", "Error fetching extras", e)
        }
    }

    override suspend fun getCategoriesWithExtrasFromDb(): List<ExtraCategoryWithExtras> {
        return extraDao.getCategoriesWithExtras()
    }
}