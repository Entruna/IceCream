package com.example.icecream.domain.repository

import com.example.icecream.data.local.entity.ExtraCategoryWithExtras

interface ExtraRepository {
    suspend fun fetchAndStoreExtras()
    suspend fun getCategoriesWithExtrasByExtraIds(extraIds: List<Long>): List<ExtraCategoryWithExtras>
    suspend fun getCategoriesWithExtrasFromDb(): List<ExtraCategoryWithExtras>
}