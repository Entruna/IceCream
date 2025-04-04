package com.example.icecream.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CartItemWithExtras(
    @Embedded val cartItem: CartItemEntity,
    @Relation(
        parentColumn = "iceCreamId",
        entityColumn = "id"
    )
    val iceCream: IceCreamEntity,
    @Relation(
        entity = ExtraEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CartExtraCrossRef::class,
            parentColumn = "cartItemId",
            entityColumn = "extraId"
        )
    )
    val extras: List<ExtraEntity>
)