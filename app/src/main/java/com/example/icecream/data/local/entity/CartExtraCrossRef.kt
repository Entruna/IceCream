package com.example.icecream.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["cartItemId", "extraId"],
    foreignKeys = [
        ForeignKey(entity = CartItemEntity::class, parentColumns = ["id"], childColumns = ["cartItemId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ExtraEntity::class, parentColumns = ["id"], childColumns = ["extraId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("extraId")]
)
data class CartExtraCrossRef(
    val cartItemId: Long,
    val extraId: Long
)