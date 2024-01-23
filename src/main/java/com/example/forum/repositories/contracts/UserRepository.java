package com.example.forum.repositories.contracts;

import com.example.forum.models.User;
import com.example.forum.models.dtos.UserDto;

import java.util.List;

public interface UserRepository {

    List<UserDto> getAll();

    User getById(int id);

    User getByUsername(String username);

    void update(User user);
}