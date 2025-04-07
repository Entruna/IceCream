package com.example.icecream.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.icecream.common.model.Status

@Entity(tableName = "ice_creams")
data class IceCreamEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val status: Status,
    val imageUrl: String?,
)
