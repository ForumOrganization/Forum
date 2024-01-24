package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.utils.PostFilterOptions;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.PostRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Post> getAll(PostFilterOptions postFilterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            postFilterOptions.getTitle().ifPresent(value -> {
                filters.add("title like :title");
                params.put("title", String.format("%%%s%%", value));
            });

            postFilterOptions.getCreatedBy().ifPresent(value -> {
                filters.add("created_by = :createdBy");
                params.put("createdBy", value);
            });

            postFilterOptions.getCreationTime().ifPresent(value -> {
                filters.add("creation_time > :creationTime");
                params.put("creationTime", value);
            });

            StringBuilder queryString = new StringBuilder("from Post");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(postFilterOptions));

            Query<Post> query = session.createQuery(queryString.toString(), Post.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public Post getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("FROM Post as p where p.id = :id", Post.class);
            query.setParameter("id", id);
            List<Post> posts = query.list();

            if (posts.isEmpty()) {
                throw new EntityNotFoundException("Post", "id", String.valueOf(id));
            }

            return posts.get(0);
        }
    }

    @Override
    public Post getByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where title = :title", Post.class);
            query.setParameter("title", title);

            List<Post> result = query.list();

            if (result.size() == 0) {
                throw new EntityNotFoundException("Post", "title", title);
            }

            return result.get(0);
        }
    }

    @Override
    public void create(Post post) {
        try (Session session = sessionFactory.openSession()) {
            post.setCreationTime(LocalDate.now());
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Post postToDelete = getById(id);

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(postToDelete);
            session.getTransaction().commit();
        }
    }

    private String generateOrderBy(PostFilterOptions postFilterOptions) {
        if (postFilterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";

        switch (postFilterOptions.getSortBy().get()) {
            case "title":
                orderBy = "title";
                break;
            case "created_by":
                orderBy = "created_by";
                break;
            case "creation_time":
                orderBy = "creation_time";
                break;
        }

        orderBy = String.format(" order by %s", orderBy);

        if (postFilterOptions.getSortOrder().isPresent() && postFilterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }
}