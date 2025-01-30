package com.example.demo.repository

import com.example.demo.network.NetworkApi
import javax.inject.Inject

class ProductRepository @Inject constructor(private val networkApi: NetworkApi) {
    suspend fun getProducts() = networkApi.getProducts()

}