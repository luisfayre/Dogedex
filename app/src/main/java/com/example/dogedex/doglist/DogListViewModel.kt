package com.example.dogedex.doglist

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogedex.models.Dog
import com.example.dogedex.api.ApiResponseStatus
import com.example.dogedex.api.repositories.DogRepository
import kotlinx.coroutines.launch

class DogListViewModel : ViewModel() {

    private val _dogList = MutableLiveData<List<Dog>>()
    val dogList: LiveData<List<Dog>>
        get() = _dogList

    private val _status = MutableLiveData<ApiResponseStatus<Any>>()
    val status: LiveData<ApiResponseStatus<Any>>
        get() = _status

    private val dogRepository = DogRepository()

    init {
        getDogCollection()
    }

    private fun getDogCollection() {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handelResponseStatus(dogRepository.getDogCollection())
        }
    }

    fun addDogToUser(dogId: Long) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handelAddDogToUserResponseStatus(dogRepository.addDogToUser(dogId))
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    @Suppress("UNCHECKED_CAST")
    private fun handelResponseStatus(apiResponseStatus: ApiResponseStatus<List<Dog>>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            _dogList.value = apiResponseStatus.data
        }

        _status.value = apiResponseStatus as ApiResponseStatus<Any>
    }

    private fun handelAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatus<Any>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            getDogCollection()
        }

        _status.value = apiResponseStatus
    }
}