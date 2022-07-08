package com.example.dogedex.api.dto.user

import com.example.dogedex.models.User

class UserDTOMapper {

     fun fromUserDTOToUserDomain(userDto: UserDTO): User {
        return User(
            userDto.id,
            userDto.email,
            userDto.authenticationToken,
        )
    }
}