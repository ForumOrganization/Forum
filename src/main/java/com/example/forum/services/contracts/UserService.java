package com.example.forum.services.contracts;

import com.example.forum.models.User;
import com.example.forum.models.dtos.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAll();

    User getById(int id);

    User getByUsername(String username);
}