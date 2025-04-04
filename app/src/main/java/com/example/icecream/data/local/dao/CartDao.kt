package com.example.icecream.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.icecream.data.local.entity.CartExtraCrossRef
import com.example.icecream.data.local.entity.CartItemEntity
import com.example.icecream.data.local.entity.CartItemWithExtras

@Dao
interface CartDao {

    @Transaction
    suspend fun addIceCreamToCartWithExtras(cartItem: CartItemEntity, extraIds: List<Long>) {
        insertCartItem(cartItem)
        extraIds.forEach { extraId ->
            insertCartExtra(CartExtraCrossRef(cartItem.id, extraId))
        }
    }

    @Insert
    suspend fun insertCartItem(cartItem: CartItemEntity)

    @Insert
    suspend fun insertCartExtra(cartExtra: CartExtraCrossRef)

    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItems(): List<CartItemEntity>

    @Transaction
    @Query("SELECT * FROM cart_items WHERE id = :cartItemId")
    suspend fun getCartItemWithExtras(cartItemId: Long): CartItemWithExtras

    @Transaction
    @Query("DELETE FROM CartExtraCrossRef WHERE cartItemId = :cartItemId AND extraId = :extraId")
    suspend fun deleteExtraFromCart(cartItemId: Long, extraId: Long)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItemEntity)
}