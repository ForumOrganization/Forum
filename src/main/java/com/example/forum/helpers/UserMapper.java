package com.example.forum.helpers;


import com.example.forum.models.User;
import com.example.forum.models.dtos.PhoneNumberDto;
import com.example.forum.models.dtos.UserDto;
import com.example.forum.models.dtos.UserResponseDto;
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

    public User fromDtoRegister(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }

    public User fromDtoUpdate(int id, UserDto dto) {
        User updatedUser = userService.getById(id);
        updatedUser.setId(id);
        updatedUser.setUsername(dto.getUsername());
        updatedUser.setFirstName(dto.getFirstName());
        updatedUser.setLastName(dto.getLastName());
        updatedUser.setEmail(dto.getEmail());
        updatedUser.setPassword(dto.getPassword());

        return updatedUser;
    }

    public UserResponseDto toDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setRole(user.getRole());
        userResponseDto.setPhoneNumber(user.getPhoneNumber());
        userResponseDto.setPosts(user.getPosts());
        userResponseDto.setComments(user.getComments());

        return userResponseDto;
    }

    public UserResponseDto toDtoRegisterAndUpdateUser(User user) {
        User userToUpdate = userService.getByUsername(user.getUsername());
        UserResponseDto userResponseDto = toDto(userToUpdate);

        return userResponseDto;
    }

    public User fromDtoUpdatePhoneNumber(int id, PhoneNumberDto dto) {
        User updatedUser = userService.getById(id);
        updatedUser.setPhoneNumber(dto.getPhoneNumber());

        return updatedUser;
    }
}