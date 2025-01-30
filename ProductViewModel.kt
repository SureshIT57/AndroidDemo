package com.example.demo.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.demo.R
import com.example.demo.repository.ProductRepository
import com.example.demo.utils.NetworkStatus
import com.example.demo.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val productRepository: ProductRepository, private val networkStatus: NetworkStatus,private val application: Application) : ViewModel(){

    fun getProducts() = liveData(Dispatchers.IO) {
        try {
            if (!networkStatus.isConnectedInternet())
                emit(Resource.error(data = null, message = application.getString(R.string.no_internet_connection)))
            else {
                emit(Resource.loading(message = "Loading",data = null))
                emit(Resource.success(data = productRepository.getProducts()))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: application.getString(R.string.something_went_wrong)))
        }
    }

}