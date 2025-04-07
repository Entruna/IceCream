package com.example.icecream.domain.repository

import com.example.icecream.data.local.entity.ExtraCategoryWithExtras

interface ExtraRepository {
    suspend fun fetchAndStoreExtras()
    suspend fun getCategoriesWithExtrasFromDb(): List<ExtraCategoryWithExtras>
}