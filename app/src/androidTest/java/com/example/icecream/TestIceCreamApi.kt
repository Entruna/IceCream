package com.example.icecream

import com.example.icecream.data.remote.IceCreamApi
import com.example.icecream.data.remote.model.ExtraCategoryDTO
import com.example.icecream.data.remote.model.ExtraDTO
import com.example.icecream.data.remote.model.IceCreamDTO
import com.example.icecream.data.remote.model.IceCreamOrderRequest
import com.example.icecream.data.remote.model.IceCreamResponse
import retrofit2.Response

class TestIceCreamApi : IceCreamApi {

    override suspend fun getIceCreams(): IceCreamResponse {
        return IceCreamResponse(
            iceCreams = listOf(
                IceCreamDTO(
                    id = 1L,
                    name = "Vanilla",
                    imageUrl = "url",
                    status = "AVAILABLE",
                )
            ),
            basePrice = 2.5
        )
    }

    override suspend fun getExtras(): List<ExtraCategoryDTO> {
        return listOf(
            ExtraCategoryDTO(
                type = "Toppings",
                required = true,
                items = listOf(
                    ExtraDTO(
                        id = 1L,
                        name = "Choco Chips",
                        price = 1.0,
                    ),
                    ExtraDTO(
                        id = 2L,
                        name = "Sprinkles",
                        price = 0.8,
                    )
                ),
            )
        )
    }
    override suspend fun submitOrder(order: List<IceCreamOrderRequest>): Response<Any> {
        if (order.any { it.id == -1L }) {
            throw RuntimeException("Invalid ice cream order")
        }
        return Response.success(Any())
    }
}
