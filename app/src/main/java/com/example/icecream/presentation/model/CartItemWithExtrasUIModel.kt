package com.example.icecream.presentation.model

data class CartItemWithExtrasUIModel(
    val cartItem: CartItemUIModel,
    val extras: List<ExtraUIModel>,
    val calculatedPrice: Double = 0.0
)

fun CartItemWithExtrasUIModel.calculatePrice(): Double {
    val iceCreamPrice = this.cartItem.iceCream.price
    val totalExtrasPrice = this.extras.sumOf { it.price }
    return iceCreamPrice + totalExtrasPrice
}
