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
    public User fromDtoUpdatePhoneNumber(int id, PhoneNumberDto dto) {
        User updatedUser = userRepository.getById(id);
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
//        if (userResponseDto.getPhoneNumber() != null) {
        userResponseDto.setPhoneNumber(user.getPhoneNumber());

//        String phoneNumber = fromDtoPhoneNumber(dto);
//        User user = userRepository.getById(id);
//        user.setPhoneNumber(phoneNumber);
//        PhoneNumber phoneNumberRepository = phoneNumberService.getPhoneNumberByUserId(id);
//        phoneNumber.setUser(phoneNumberRepository.getUser());

        return userResponseDto;
    }

//    public String fromDtoPhoneNumber(PhoneNumberDto dto) {
//        User user = new User();
//         user.setPhoneNumber(dto.getPhoneNumber());
//
//        return user.getPhoneNumber();
//    }
}