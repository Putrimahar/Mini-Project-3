package com.putrimaharani0087.miniproject3.ui.screen

import android.graphics.Bitmap
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Makanan>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retriveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = MakananApi.service.getMakanan(userId)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, nama: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = MakananApi.service.postMakanan(
                    userId,
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.status == "success")
                    retriveData(userId)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }

    fun deleteData(userId: String, makananId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = MakananApi.service.deleteMakanan(
                    userId,
                    makananId
                )

                if(result.status == "success"){
                    retriveData(userId)
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "Error delete: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun updateData(userId: String, makananId: String, nama: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = MakananApi.service.editMakanan(
                    userId,
                    makananId,
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if(result.status == "success")
                    retriveData(userId)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "gambar", "image.jpg", requestBody)
    }

    fun clearMessage() { errorMessage.value = null}
}