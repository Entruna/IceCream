package com.example.icecream.presentation.model

data class CartItemWithExtrasUIModel(
    val cartItem: CartItemUIModel,
    val extras: List<ExtraUIModel>
)
