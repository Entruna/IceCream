package com.example.icecream.data.remote

import com.example.icecream.data.remote.model.ExtraTypeDTO
import com.example.icecream.data.remote.model.IceCreamOrderRequest
import com.example.icecream.data.remote.model.IceCreamResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IceCreamApi {

    @GET("icecreams")
    suspend fun getIceCreams(): IceCreamResponse

    @GET("extras")
    suspend fun getExtras(): List<ExtraTypeDTO>

    @POST("post")
    suspend fun submitOrder(@Body order: List<IceCreamOrderRequest>): Response<Any>
}