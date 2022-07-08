package com.example.dogedex.api.repositories

import com.example.dogedex.models.User
import com.example.dogedex.api.ApiResponseStatus
import com.example.dogedex.api.DogsApi
import com.example.dogedex.api.dto.auth.LoginDTO
import com.example.dogedex.api.makeNetworkCall
import com.example.dogedex.api.dto.auth.SignUpDTO
import com.example.dogedex.api.dto.user.UserDTOMapper

class AuthRepository {

    suspend fun login(
        email: String,
        password: String,
    ): ApiResponseStatus<User> = makeNetworkCall {
        val loginDTO = LoginDTO(email, password)
        val loginResponse = DogsApi.retrofitService.login(loginDTO)

        if (!loginResponse.isSuccess) {
            throw Exception(loginResponse.message)
        }

        val userDTO = loginResponse.data.user
        val userDTOMapper = UserDTOMapper()
        userDTOMapper.fromUserDTOToUserDomain(userDTO)
    }

    suspend fun signUp(
        email: String,
        password: String,
        passwordConfirmation: String
    ): ApiResponseStatus<User> = makeNetworkCall {
        val signUpDTO = SignUpDTO(email, password, passwordConfirmation)
        val signUpResponse = DogsApi.retrofitService.signUp(signUpDTO)

        if (!signUpResponse.isSuccess) {
            throw Exception(signUpResponse.message)
        }

        val userDTO = signUpResponse.data.user
        val userDTOMapper = UserDTOMapper()
        userDTOMapper.fromUserDTOToUserDomain(userDTO)
    }
}