package com.example.icecream.data.remote

import com.example.icecream.data.remote.model.ExtraCategoryDTO
import com.example.icecream.data.remote.model.IceCreamOrderRequest
import com.example.icecream.data.remote.model.IceCreamResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IceCreamApi {

    @GET("https://raw.githubusercontent.com/udemx/hr-resources/master/icecreams.json")
    suspend fun getIceCreams(): IceCreamResponse

    @GET("https://raw.githubusercontent.com/udemx/hr-resources/master/extras.json")
    suspend fun getExtras(): List<ExtraCategoryDTO>

    @POST("post")
    suspend fun submitOrder(@Body order: List<IceCreamOrderRequest>): Response<Any>
}