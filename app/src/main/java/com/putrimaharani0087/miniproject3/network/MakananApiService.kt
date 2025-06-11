package com.putrimaharani0087.miniproject3.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://terrapin-precious-wildly.ngrok-free.app/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface MakananApiService {
    @GET("makanan")
    suspend fun getMakanan(): String
}

object MakananApi {
    val service: MakananApiService by lazy {
        retrofit.create(MakananApiService::class.java)
    }
}