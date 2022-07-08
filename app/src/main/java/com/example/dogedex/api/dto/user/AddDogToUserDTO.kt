package com.example.dogedex.api.dto.user

import com.squareup.moshi.Json

class AddDogToUserDTO(
    @field:Json(name = "dog_id") val dogId: Long
)