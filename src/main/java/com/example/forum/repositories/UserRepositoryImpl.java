package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.UserRepository;
import com.example.forum.utils.UserFilterOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAll(UserFilterOptions userFilterOptions) {
        try (Session session = sessionFactory.openSession()) {

            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();


            userFilterOptions.getUsername().ifPresent(value -> {
                filters.add("username like :username");
                params.put("username", String.format("%%%s%%", value));
            });

            userFilterOptions.getFirstName().ifPresent(value -> {
                filters.add(" firstName like :firstName ");
                params.put("firstName", String.format("%%%s%%", value));
            });

            userFilterOptions.getFirstName().ifPresent(value -> {
                filters.add(" firstName like :firstName ");
                params.put("firstName", String.format("%%%s%%", value));
            });

            userFilterOptions.getLastName().ifPresent(value -> {
                filters.add(" lastName like :lastName ");
                params.put("lastName", String.format("%%%s%%", value));
            });

            userFilterOptions.getEmail().ifPresent(value -> {
                filters.add(" email like :email ");
                params.put("email", String.format("%%%s%%", value));
            });

            userFilterOptions.getRole().ifPresent(value -> {
                filters.add(" role = :role ");
                params.put("role", value);
            });
            userFilterOptions.getRole().ifPresent(value -> {
                filters.add(" status = :status ");
                params.put("status", value);
            });

            StringBuilder queryString = new StringBuilder("from User");

            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }

            queryString.append(generateOrderBy(userFilterOptions));

            Query<User> query = session.createQuery(queryString.toString(), User.class);
            query.setProperties(params);

            return query.list();
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
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);

            List<User> result = query.list();

            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "username", username);
            }

            return result.get(0);
        }
    }

    @Override
    public User getByUsernameFindUser(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);

            List<User> result = query.list();

            return result.isEmpty() ? null : result.get(0);
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);

            List<User> result = query.list();

            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "email", email);
            }

            return result.get(0);
        }
    }

    @Override
    public User getByEmailFindUser(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);

            List<User> result = query.list();

            return result.isEmpty() ? null : result.get(0);
        }
    }

    @Override
    public List<User> getByFirstName(String firstName) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where firstName = :firstName", User.class);
            query.setParameter("firstName", firstName);

            List<User> result = query.list();

            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "first name", firstName);
            }

            return result;
        }
    }

    @Override
    public User getUserByComment(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("select user from Comment c where c.id = :id", User.class);
            query.setParameter("id", id);

            List<User> result = query.list();

            if (result.isEmpty()) {
                throw new EntityNotFoundException("Comment", "id", String.valueOf(id));
            }

            return result.get(0);
        }
    }

    @Override
    public List<Post> getPosts(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("FROM Post as p where p.createdBy.id = :userId", Post.class);
            query.setParameter("userId", userId);

            List<Post> posts = query.list();
            getById(userId);

            if (posts.isEmpty()) {
                throw new EntityNotFoundException("User posts", "userId", String.valueOf(userId));
            }

            return posts.stream().toList();
        }
    }

    @Override
    public void registerUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updateUser(User targetUser) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(targetUser);
            session.getTransaction().commit();
        }
    }

    @Override
    public void reactivated(User targetUser) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            for (Post post : targetUser.getPosts()) {
                for (Comment comment : post.getComments()) {
                    comment.setDeleted(false);
                    session.merge(comment);
                }

                post.setDeleted(false);
                session.merge(post);
            }

            targetUser.setDeleted(false);
            session.merge(targetUser);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteUser(int targetUserId) {
        User userToDelete = getById(targetUserId);

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            for (Post post : userToDelete.getPosts()) {
                for (Comment comment : post.getComments()) {
                    comment.setDeleted(true);
                    session.merge(comment);
                }

                post.setDeleted(true);
                session.merge(post);
            }

//            userToDelete.setDeleted(true);
//            session.merge(userToDelete);
            session.remove(userToDelete);
            session.getTransaction().commit();
        }
    }

    private String generateOrderBy(UserFilterOptions userFilterOptions) {
        if (userFilterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (userFilterOptions.getSortBy().get()) {
            case "username":
                orderBy = "username";
                break;
            case "firstName":
                orderBy = "firstName";
                break;
            case "lastName":
                orderBy = "lastName";
                break;
            case "email":
                orderBy = "email";
                break;
            case "role":
                orderBy = "role";
                break;
            case "status":
                orderBy = "status";
                break;
            default:
                return "";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (userFilterOptions.getSortOrder().isPresent() && userFilterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }

    public boolean isDataBaseEmpty() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u", Long.class);
            Long userCount = query.uniqueResult();

            return userCount == 0;
        }
    }

    public boolean existsByPhoneNumber(User userPhoneNumberToBeUpdate) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User  u WHERE u.phoneNumber = :phoneNumber", Long.class);
            query.setParameter("phoneNumber", userPhoneNumberToBeUpdate.getPhoneNumber());
            Long userCount = query.uniqueResult();

            return userCount != null && userCount > 0;
        }
    }
}