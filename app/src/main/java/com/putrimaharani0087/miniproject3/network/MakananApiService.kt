package com.putrimaharani0087.miniproject3.network

import com.putrimaharani0087.miniproject3.model.Makanan
import com.putrimaharani0087.miniproject3.model.OpStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

private const val BASE_URL = "https://asessment3-api-production.up.railway.app/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MakananApiService {
    @GET("makanan")
    suspend fun getMakanan(
        @Header("Authorization") userId: String
    ): List<Makanan>

    @Multipart
    @POST("makanan/store")
    suspend fun postMakanan(
        @Header("Authorization") userId: String,
        @Part("nama") nama: RequestBody,
        @Part gambar: MultipartBody.Part
    ): OpStatus

    @DELETE("makanan/delete/{id}")
    suspend fun deleteMakanan(
        @Header("Authorization") userId: String,
        @Path("id") id: String
    ): OpStatus

    @Multipart
    @POST("makanan/edit/{id}")
    suspend fun editMakanan(
        @Header("Authorization") userId: String,
        @Path("id") id: String,
        @Part("nama") nama: RequestBody,
        @Part gambar: MultipartBody.Part
    ): OpStatus
}

object MakananApi {
    val service: MakananApiService by lazy {
        retrofit.create(MakananApiService::class.java)
    }

    fun getMakananUrl(gambar: String): String {
        return "https://asessment3-api-production.up.railway.app/storage/$gambar"
    }
}

enum class ApiStatus{ LOADING, SUCCESS, FAILED}