package com.example.forum.helpers;


import com.example.forum.models.User;
import com.example.forum.models.dtos.PhoneNumberDto;
import com.example.forum.models.dtos.UserDto;
import com.example.forum.models.dtos.UserResponseDto;
import com.example.forum.repositories.contracts.UserRepository;
import com.example.forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final UserService userService;

    private final UserRepository userRepository;

    @Autowired
    public UserMapper(UserService userService,  UserRepository userRepository) {
        this.userService = userService;

        this.userRepository = userRepository;
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
            userResponseDto.setPhoneNumber(user.getPhoneNumber());
        }

        userResponseDto.setPosts(user.getPosts());

        return userResponseDto;
    }
    public String fromDtoPhoneNumber(int id, PhoneNumberDto dto) {
        String phoneNumber = fromDtoPhoneNumber(dto);
        User user = userRepository.getById(id);
        user.setPhoneNumber(phoneNumber);
//        PhoneNumber phoneNumberRepository = phoneNumberService.getPhoneNumberByUserId(id);
//        phoneNumber.setUser(phoneNumberRepository.getUser());

        return phoneNumber;
    }

    public String fromDtoPhoneNumber(PhoneNumberDto dto) {
        User user = new User();
         user.setPhoneNumber(dto.getPhoneNumber());

        return user.getPhoneNumber();
    }
}