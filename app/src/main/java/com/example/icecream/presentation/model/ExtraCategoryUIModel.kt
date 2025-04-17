package com.example.icecream.presentation.model

data class ExtraCategoryUIModel(
    val id: Long,
    val type: String,
    val required: Boolean?,
    val nameResId: Int?
)
