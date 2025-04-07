package com.example.icecream.domain.repository

import com.example.icecream.data.local.entity.CartItemWithExtras
import com.example.icecream.data.remote.model.IceCreamOrderRequest
import retrofit2.Response

interface CartRepository {
    suspend fun getAllCartItemsWithExtras(): List<CartItemWithExtras>
    suspend fun submitOrder(order: List<IceCreamOrderRequest>): Response<Any>
}