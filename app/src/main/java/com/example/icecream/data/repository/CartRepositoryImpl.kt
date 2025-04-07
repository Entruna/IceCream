package com.example.icecream.data.repository

import com.example.icecream.data.local.dao.CartDao
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


    override suspend fun getAllCartItemsWithExtras(): List<CartItemWithExtras> {
        return cartDao.getAllCartItemsWithExtras()
    }

    override suspend fun submitOrder(order: List<IceCreamOrderRequest>): Response<Any> {
        return iceCreamApi.submitOrder(order)
    }
}