package com.example.dogedex.api.dto

import com.example.dogedex.models.Dog

class DogDTOMapper {

     fun fromDogDTOToDogDomain(dogDto: DogDTO): Dog {
        return Dog(
            dogDto.id,
            dogDto.index,
            dogDto.name,
            dogDto.type,
            dogDto.heightFemale,
            dogDto.heightMale,
            dogDto.imageUrl,
            dogDto.lifeExpectancy,
            dogDto.temperament,
            dogDto.weightFemale,
            dogDto.weighMale,
        )
    }

    fun fromDogDTOListToDogDomainList(dogDtoList: List<DogDTO>): List<Dog> {
        return dogDtoList.map { fromDogDTOToDogDomain(it) }
    }
}