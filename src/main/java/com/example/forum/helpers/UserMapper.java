package com.example.forum.helpers;

import com.example.forum.models.PhoneNumber;
import com.example.forum.models.User;
import com.example.forum.models.dtos.PhoneNumberDto;
import com.example.forum.models.dtos.UserDto;
import com.example.forum.models.dtos.UserResponseDto;
import com.example.forum.services.contracts.PhoneNumberService;
import com.example.forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final UserService userService;
    private final PhoneNumberService phoneNumberService;

    @Autowired
    public UserMapper(UserService userService, PhoneNumberService phoneNumberService) {
        this.userService = userService;
        this.phoneNumberService = phoneNumberService;
    }

    public User fromDto(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }

    public User fromDtoUpdate(int id, UserDto dto) {

        User updatedUser = new User();
        updatedUser.setId(id);
        updatedUser.setFirstName(dto.getFirstName());
        updatedUser.setLastName(dto.getLastName());
        updatedUser.setEmail(dto.getEmail());
        updatedUser.setPassword(dto.getPassword());
        updatedUser.setUsername(dto.getUsername());

        return updatedUser;
    }

    public static UserResponseDto toDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setRole(user.getRole());
        if (userResponseDto.getPhoneNumber() != null) {
            userResponseDto.setPhoneNumber(userResponseDto.getPhoneNumber());
        }
        userResponseDto.setPosts(user.getPosts());

        return userResponseDto;
    }
}