package com.example.icecream.domain.repository

import com.example.icecream.data.local.entity.CartItemEntity
import com.example.icecream.data.local.entity.CartItemWithExtras
import com.example.icecream.data.remote.model.IceCreamOrderRequest
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CartRepository {
    suspend fun addIceCreamWithExtras(cartItem: CartItemEntity, extraIds: List<Long>)
    fun getAllCartItemsWithExtrasFlow(): Flow<List<CartItemWithExtras>>
    suspend fun updateCartItemExtras(cartItem: CartItemEntity, selectedExtras: List<Long>)
    suspend fun removeIceCream(cartItem: CartItemEntity)
    suspend fun removeExtra(cartItemId: Long, extraId: Long)
    suspend fun submitOrder(order: List<IceCreamOrderRequest>): Response<Any>
    suspend fun clearCart()
}