package com.example.dogedex.dogdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogedex.R
import com.example.dogedex.api.ApiResponseStatus
import com.example.dogedex.api.repositories.DogRepository
import kotlinx.coroutines.launch

class DogDetailViewModel : ViewModel() {

    var status = mutableStateOf<ApiResponseStatus<Any>?>(null)
        private set

    private val dogRepository = DogRepository()

    fun addDogToUser(dogId: Long) {
        viewModelScope.launch {
            status.value = ApiResponseStatus.Loading()
            handelAddDogToUserResponseStatus(dogRepository.addDogToUser(dogId))
        }
    }

    private fun handelAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatus<Any>) {
        status.value = apiResponseStatus
    }

    fun resetApiResponseStatus() {
        status.value = null
    }
}