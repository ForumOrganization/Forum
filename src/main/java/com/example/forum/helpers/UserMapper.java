package com.example.forum.helpers;


import com.example.forum.models.User;
import com.example.forum.models.dtos.PhoneNumberDto;
import com.example.forum.models.dtos.UserDto;
import com.example.forum.models.dtos.UserResponseDto;
import com.example.forum.models.enums.Role;
import com.example.forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final UserService userService;

    @Autowired
    public UserMapper(UserService userService) {
        this.userService = userService;
    }

    public User fromDto(UserDto dto) {
        User user = new User();
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

        return updatedUser;
    }

    public User fromDtoUpdateToAdmin(int id, User user) {
        User updatedUser = userService.getById(id, user);
        updatedUser.setRole(Role.ADMIN);

        return updatedUser;
    }

    public static UserResponseDto toDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setRole(user.getRole());
        userResponseDto.setPhoneNumber(user.getPhoneNumber());
        userResponseDto.setPosts(user.getPosts());

        return userResponseDto;
    }

    public User fromDtoUpdatePhoneNumber(int id, PhoneNumberDto dto, User user) {
        User updatedUser = userService.getById(id, user);
        updatedUser.setPhoneNumber(dto.getPhoneNumber());

        return updatedUser;
    }

    public static UserResponseDto toDtoPhoneNumber(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPosts(user.getPosts());
        userResponseDto.setRole(user.getRole());
        userResponseDto.setPhoneNumber(user.getPhoneNumber());

        return userResponseDto;
    }
}