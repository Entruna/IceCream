package com.example.icecream.presentation.model

import com.example.icecream.common.model.Status

data class IceCreamUIModel(
    val id: Long,
    val name: String,
    val imageUrl: String?,
    val status: Status,
    val price: Double,
    val nameResId: Int?
)