package com.example.forum.helpers;

import com.example.forum.models.User;
import com.example.forum.models.dtos.UserDto;
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

    public User fromDto(int id, UserDto dto) {
        User user = fromDto(dto);
        user.setId(id);
        User repostioryUser = userService.getById(id);
        user.setFirstName(repostioryUser.getFirstName());
        user.setLastName(repostioryUser.getLastName());
        user.setEmail(repostioryUser.getEmail());
        user.setPassword(repostioryUser.getPassword());

        return user;
    }

    public User fromDto(UserDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }
}