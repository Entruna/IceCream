package com.example.icecream.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ExtraCategoryWithExtras(
    @Embedded val category: ExtraCategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val extras: List<ExtraEntity>
)
