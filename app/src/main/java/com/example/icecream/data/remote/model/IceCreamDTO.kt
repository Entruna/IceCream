package com.example.icecream.data.remote.model

data class IceCreamDTO(
    val id: Long,
    val name: String,
    val status: String,
    val imageUrl: String?
)

data class IceCreamResponse(
    val iceCreams: List<IceCreamDTO>,
    val basePrice: Double
)
