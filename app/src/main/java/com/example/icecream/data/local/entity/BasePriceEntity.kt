package com.example.icecream.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "base_price")
data class BasePriceEntity(
    @PrimaryKey val id: Int = 0,
    val price: Double
)