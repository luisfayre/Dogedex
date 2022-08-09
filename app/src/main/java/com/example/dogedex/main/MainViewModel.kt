package com.example.dogedex.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogedex.api.ApiResponseStatus
import com.example.dogedex.api.repositories.DogRepository
import com.example.dogedex.models.Dog
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _dog = MutableLiveData<Dog>()
    val dog: LiveData<Dog>
        get() = _dog

    private val _status = MutableLiveData<ApiResponseStatus<Dog>>()
    val status: LiveData<ApiResponseStatus<Dog>>
        get() = _status

    private val dogRepository = DogRepository()

    fun getDogByMlId(mlDogId: String) {
        // Corrutina
        viewModelScope.launch {
            handelResponseStatus(dogRepository.getDogByMlId(mlDogId))
        }
    }

    private fun handelResponseStatus(apiResponseStatus: ApiResponseStatus<Dog>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            _dog.value = apiResponseStatus.data!!
        }
        _status.value = apiResponseStatus
    }


}