package repositories;

import models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    void update(User user);
}