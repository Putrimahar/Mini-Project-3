package com.putrimaharani0087.miniproject3.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.putrimaharani0087.miniproject3.model.Makanan
import com.putrimaharani0087.miniproject3.network.ApiStatus
import com.putrimaharani0087.miniproject3.network.MakananApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Makanan>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    init {
        retrieveData()
    }

    fun retrieveData() {
        viewModelScope.launch (Dispatchers.IO){
            status.value = ApiStatus.LOADING
            try {
                data.value = MakananApi.service.getMakanan()
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }
}