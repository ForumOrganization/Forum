package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.dtos.UserDto;
import com.example.forum.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<UserDto> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", UserDto.class)
                    .list();
        }
    }

    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);

            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }

            return user;
        }
    }

    @Override
    public com.example.forum.models.User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<com.example.forum.models.User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);

            List<com.example.forum.models.User> result = query.list();

            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "username", username);
            }

            return result.get(0);
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);

            List<User> result = query.list();

            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "email", email);
            }

            return result.get(0);
        }
    }

    @Override
    public User getByFirstName(String firstName) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where firstName = :firstName", User.class);
            query.setParameter("firstName", firstName);

            List<User> result = query.list();

            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "first name", firstName);
            }

            return result.get(0);
        }
    }


    @Override
    public List<Post> getPosts(int id, User user) {
        return null;
    }

    @Override
    public void update(User targetUser) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(targetUser);
            session.getTransaction().commit();
        }
    }

    @Override
    public User updateToAdmin(User userAdmin, User updateToAdmin) {
        return null;
    }

    @Override
    public void addPhoneNumberToAdmin(User admin, String phoneNumber) {

    }
}