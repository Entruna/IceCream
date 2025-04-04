package com.example.icecream.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "extras",
    foreignKeys = [ForeignKey(
        entity = ExtraCategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("categoryId")]
)
data class ExtraEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val price: Double,
    val categoryId: Long
)