package com.example.dogedex.api

import com.example.dogedex.*
import com.example.dogedex.api.dto.auth.LoginDTO
import com.example.dogedex.api.dto.auth.SignUpDTO
import com.example.dogedex.api.dto.user.AddDogToUserDTO
import com.example.dogedex.api.responses.DefaultResponse
import com.example.dogedex.api.responses.DogListApiResponse
import com.example.dogedex.api.responses.auth.AuthApiResponse
import com.example.dogedex.api.responses.dogs.DogApiResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private val okHttpClient = OkHttpClient
    .Builder()
    .addInterceptor(ApiServiceInterceptor)
    .build()

private var retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface ApiService {
    @GET(GET_ALL_DOGS_URL)
    suspend fun getAllDogs(): DogListApiResponse

    @POST(SIGN_UP_URL)
    suspend fun signUp(@Body signUpDTO: SignUpDTO): AuthApiResponse

    @POST(SIGN_IN_URL)
    suspend fun login(@Body loginDTO: LoginDTO): AuthApiResponse

    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @POST(ADD_DOG_TO_USER_URL)
    suspend fun addDogToUser(@Body addDogToUserDTO: AddDogToUserDTO): DefaultResponse

    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @GET(GET_USER_DOGS_URL)
    suspend fun getUserDogs(): DogListApiResponse

    @GET(GET_DOG_VY_ML_ID)
    suspend fun getDogByMlId(@Query("ml_id") mlId:String): DogApiResponse
}

object DogsApi {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}