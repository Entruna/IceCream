package com.example.icecream.data.remote.model

data class ExtraDTO(
    val id: Long,
    val name: String,
    val price: Double
)


data class ExtraTypeDTO(
    val type: String,
    val required: Boolean,
    val items: List<ExtraDTO>
)
