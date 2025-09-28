package com.example.icecream.data.repository

import com.example.icecream.data.local.dao.ExtraDao
import com.example.icecream.data.local.entity.ExtraCategoryWithExtras
import com.example.icecream.data.mapper.ExtraMapper
import com.example.icecream.data.remote.IceCreamApi
import com.example.icecream.domain.exception.DataFetchException
import com.example.icecream.domain.repository.ExtraRepository

class ExtraRepositoryImpl(
    private val extraDao: ExtraDao,
    private val iceCreamApi: IceCreamApi,
    private val extraMapper: ExtraMapper
) : ExtraRepository {


    override suspend fun fetchAndStoreExtras() {
        try {
            val response = iceCreamApi.getExtras()

            val existingCategories = extraDao.getAllCategories()

            val categoryEntities = response.map { extraMapper.mapToExtraCategoryEntity(it) }

            val insertedIds = extraDao.insertCategories(categoryEntities)

            val typeToIdMap = categoryEntities.zip(insertedIds)
                .mapNotNull { (category, id) ->
                    if (id != -1L) category.type to id else null
                }.toMap()
                .toMutableMap()

            existingCategories.forEach { existing ->
                typeToIdMap[existing.type] = existing.id
            }

            val extras = response.flatMap { categoryDTO ->
                val categoryId = typeToIdMap[categoryDTO.type] ?: return@flatMap emptyList()
                categoryDTO.items.map { itemDTO ->
                    extraMapper.mapToExtraEntity(itemDTO, categoryId)
                }
            }

            extraDao.insertExtras(extras)


        } catch (e: Exception) {
            throw DataFetchException("Failed to fetch extras", e)
        }
    }

    override suspend fun getCategoriesWithExtrasByExtraIds(extraIds: List<Long>): List<ExtraCategoryWithExtras> {
        return extraDao.getCategoriesWithExtrasByExtraIds(extraIds)
    }

    override suspend fun getCategoriesWithExtrasFromDb(): List<ExtraCategoryWithExtras> {
        return extraDao.getCategoriesWithExtras()
    }
}