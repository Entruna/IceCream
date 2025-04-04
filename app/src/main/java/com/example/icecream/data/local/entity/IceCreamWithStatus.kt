package com.example.icecream.data.local.entity

import androidx.room.Embedded

data class IceCreamWithStatus(
    @Embedded val iceCream: IceCreamEntity
)
