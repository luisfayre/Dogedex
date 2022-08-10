package com.example.dogedex.api.responses.dogs

import com.squareup.moshi.Json

class DogApiResponse(
    val message: String,
    @field:Json(name = "is_success")
    val isSuccess: Boolean,
    val data: DogResponse
)