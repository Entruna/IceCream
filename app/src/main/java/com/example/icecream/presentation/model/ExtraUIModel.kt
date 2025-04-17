package com.example.icecream.presentation.model

data class ExtraUIModel(
    val id: Long,
    val name: String,
    val price: Double,
    val category: ExtraCategoryUIModel,
    val nameResId: Int?
)