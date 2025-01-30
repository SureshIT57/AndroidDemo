package com.example.demo.network

import com.example.demo.response.GetProductResponse
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface NetworkApi {

    @GET("products")
    suspend fun getProducts():GetProductResponse
}