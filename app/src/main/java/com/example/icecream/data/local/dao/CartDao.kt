package com.example.icecream.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.icecream.data.local.entity.CartExtraCrossRef
import com.example.icecream.data.local.entity.CartItemEntity
import com.example.icecream.data.local.entity.CartItemWithExtras

@Dao
interface CartDao {

    @Transaction
    suspend fun addIceCreamToCartWithExtras(cartItem: CartItemEntity, extraIds: List<Long>) {
        val cartItemId = insertCartItem(cartItem)
        extraIds.forEach { extraId ->
            insertCartExtra(CartExtraCrossRef(cartItemId, extraId))
        }
    }

    @Insert
    suspend fun insertCartItem(cartItem: CartItemEntity) : Long

    @Insert
    suspend fun insertCartExtra(cartExtra: CartExtraCrossRef)


    @Update
    suspend fun updateCartItem(cartItem: CartItemEntity)

    @Transaction
    suspend fun updateCartItemExtras(cartItem: CartItemEntity, extraIds: List<Long>) {
        deleteExtrasByCartItemId(cartItem.id)

        updateCartItem(cartItem)

        extraIds.forEach { extraId ->
            insertCartExtra(CartExtraCrossRef(cartItem.id, extraId))
        }
    }

    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItems(): List<CartItemEntity>

    @Transaction
    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItemsWithExtras(): List<CartItemWithExtras>

    @Transaction
    @Query("DELETE FROM CartExtraCrossRef WHERE cartItemId = :cartItemId AND extraId = :extraId")
    suspend fun deleteExtraFromCart(cartItemId: Long, extraId: Long)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItemEntity)

    @Query("DELETE FROM CartExtraCrossRef WHERE cartItemId = :cartItemId")
    suspend fun deleteExtrasByCartItemId(cartItemId: Long)

    @Query("DELETE FROM cart_items")
    suspend fun deleteAllCartItems()
}