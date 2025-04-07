package com.example.icecream.data.remote.model

data class IceCreamOrderRequest(
    val id: Long,
    val extra: List<Long>
)