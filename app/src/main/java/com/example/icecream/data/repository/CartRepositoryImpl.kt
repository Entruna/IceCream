package com.example.icecream.data.repository

import com.example.icecream.data.local.dao.CartDao
import com.example.icecream.data.local.entity.CartItemEntity
import com.example.icecream.data.local.entity.CartItemWithExtras
import com.example.icecream.data.remote.IceCreamApi
import com.example.icecream.data.remote.model.IceCreamOrderRequest
import com.example.icecream.domain.repository.CartRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val iceCreamApi: IceCreamApi
) : CartRepository {
    override suspend fun addIceCreamWithExtras(cartItem: CartItemEntity, extraIds: List<Long>) {
        return cartDao.addIceCreamToCartWithExtras(cartItem, extraIds)
    }

    override suspend fun getAllCartItemsWithExtras(): List<CartItemWithExtras> {
        return cartDao.getAllCartItemsWithExtras()
    }

    override suspend fun updateCartItemExtras(cartItem: CartItemEntity, selectedExtras: List<Long>) {
        cartDao.updateCartItemExtras(cartItem, selectedExtras)
    }

    override suspend fun removeIceCream(cartItem: CartItemEntity) {
        cartDao.deleteCartItem(cartItem)
    }

    override suspend fun removeExtra(cartItemId: Long, extraId: Long) {
        cartDao.deleteExtraFromCart(cartItemId, extraId)
    }

    override suspend fun submitOrder(order: List<IceCreamOrderRequest>): Response<Any> {
        return iceCreamApi.submitOrder(order)
    }

    override suspend fun clearCart() {
        cartDao.deleteCartItems()
    }
}