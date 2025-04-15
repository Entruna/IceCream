package com.example.icecream.data.local.entity

import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "extra_categories",
    indices = [Index(value = ["type"], unique = true)]
)
data class ExtraCategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val required: Boolean? = null,
    @StringRes val nameResId: Int? = null,

    )