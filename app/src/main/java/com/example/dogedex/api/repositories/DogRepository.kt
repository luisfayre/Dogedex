package com.example.dogedex.api.repositories

import android.util.Log
import com.example.dogedex.R
import com.example.dogedex.models.Dog
import com.example.dogedex.api.ApiResponseStatus
import com.example.dogedex.api.DogsApi.retrofitService
import com.example.dogedex.api.makeNetworkCall
import com.example.dogedex.api.dto.DogDTOMapper
import com.example.dogedex.api.dto.user.AddDogToUserDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class DogRepository {

    suspend fun getDogCollection(): ApiResponseStatus<List<Dog>> {
        return withContext(Dispatchers.IO) {
            val allDogsListDeperedResponse = async { downloadDogs() }
            val userDogListDeperedResponse = async { getUserDogs() }

            val allDogsListResponse = allDogsListDeperedResponse.await()
            val userDogListResponse = userDogListDeperedResponse.await()

            if (allDogsListResponse is ApiResponseStatus.Error) {
                allDogsListResponse
            } else if (allDogsListResponse is ApiResponseStatus.Error) {
                userDogListResponse
            } else if (allDogsListResponse is ApiResponseStatus.Success &&
                userDogListResponse is ApiResponseStatus.Success
            ) {

                ApiResponseStatus.Success(
                    getCollectionList(
                        allDogsListResponse.data,
                        userDogListResponse.data
                    )
                )
            } else {
                ApiResponseStatus.Error(R.string.unknown_error)
            }

        }
    }

    private fun getCollectionList(allDogList: List<Dog>, userDogList: List<Dog>): List<Dog> {
        return allDogList.map {
            if (userDogList.contains(it)) {
                it
            } else {
                Dog(
                    0, it.index, "", "",
                    "", "", "",
                    "", "", "",
                    "", inCollection = false
                )
            }
        }.sorted()
    }

    private suspend fun downloadDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
        val dogListApiResponse = retrofitService.getAllDogs()
        val dogDTOList = dogListApiResponse.data.dogs
        val dogDTOMapper = DogDTOMapper()
        dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
    }

    suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> = makeNetworkCall {
        val addDogToUserDTO = AddDogToUserDTO(dogId)
        val defaultResponse = retrofitService.addDogToUser(addDogToUserDTO)
        Log.wtf("asdasd", defaultResponse.isSuccess.toString())


        if (!defaultResponse.isSuccess) {
            throw Exception(defaultResponse.message)
        }
    }

    private suspend fun getUserDogs(): ApiResponseStatus<List<Dog>> = makeNetworkCall {
        val dogListApiResponse = retrofitService.getUserDogs()
        val dogDTOList = dogListApiResponse.data.dogs
        val dogDTOMapper = DogDTOMapper()
        dogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
    }
}