package com.putrimaharani0087.miniproject3.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.putrimaharani0087.miniproject3.network.MakananApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    init {
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch (Dispatchers.IO){
            try {
                val result = MakananApi.service.getMakanan()
                Log.d("MainViewModel", "Success: $result")
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }
}