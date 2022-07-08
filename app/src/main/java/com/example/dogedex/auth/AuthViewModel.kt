package com.example.dogedex.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogedex.api.ApiResponseStatus
import com.example.dogedex.api.repositories.AuthRepository
import com.example.dogedex.models.User
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _status = MutableLiveData<ApiResponseStatus<User>>()
    val status: LiveData<ApiResponseStatus<User>>
        get() = _status

    private val authRepository = AuthRepository()


    fun signUp(
        email: String,
        password: String,
        passwordConfirmation: String
    ) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handelResponseStatus(authRepository.signUp(email, password, passwordConfirmation))
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handelResponseStatus(authRepository.login(email, password))
        }
    }

    private fun handelResponseStatus(apiResponseStatus: ApiResponseStatus<User>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            _user.value = apiResponseStatus.data!!
        }

        _status.value = apiResponseStatus
    }


}