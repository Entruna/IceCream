package com.example.icecream.domain.repository

import com.example.icecream.data.local.entity.ExtraCategoryWithExtras
import kotlinx.coroutines.flow.Flow

interface ExtraRepository {
    suspend fun fetchAndStoreExtras()
    suspend fun getCategoriesWithExtrasByExtraIds(extraIds: List<Long>): List<ExtraCategoryWithExtras>
    fun getCategoriesWithExtrasFlow(): Flow<List<ExtraCategoryWithExtras>>
}