package services;

import models.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);
}