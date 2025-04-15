package com.example.icecream.presentation.model

sealed class CartItemUIModel {
    abstract val iceCream: IceCreamUIModel

    data class New(
        override val iceCream: IceCreamUIModel
    ) : CartItemUIModel()

    data class Existing(
        val id: Long,
        override val iceCream: IceCreamUIModel
    ) : CartItemUIModel()
}