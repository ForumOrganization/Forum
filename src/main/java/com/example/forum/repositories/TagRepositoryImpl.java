package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.Reaction_comments;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.repositories.contracts.TagRepository;
import com.example.forum.utils.TagFilterOptions;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private SessionFactory sessionFactory;
    private PostRepository postRepository;

    @Autowired
    public TagRepositoryImpl(SessionFactory sessionFactory, PostRepository postRepository) {
        this.sessionFactory = sessionFactory;
        this.postRepository = postRepository;
    }

    @Override
    public List<Tag> getAllTags(TagFilterOptions tagFilterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();


            tagFilterOptions.getName().ifPresent(value -> {
                filters.add("name like :name");
                params.put("name", String.format("%%%s%%", value));
            });


            StringBuilder queryString = new StringBuilder("from Tag");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(tagFilterOptions));

            Query<Tag> query = session.createQuery(queryString.toString(), Tag.class);
            query.setProperties(params);
            List<Tag> list = query.list();

            if (list.isEmpty()) {
                throw new EntityNotFoundException("Tag", "id");
            }

            return list;
        }
    }


    @Override
    public Tag getTagById(int tagId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Tag> query = session.createQuery(
                    "SELECT t FROM Tag t WHERE t.id = :tagId", Tag.class);

            query.setParameter("tagId", tagId)
                    .getResultList();

            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("Tag", "this id");
            }

            return query.list().get(0);
        }
    }

    @Override
    public Tag getTagByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Tag> query = session.createQuery(
                    "SELECT t FROM Tag t WHERE t.name = :name", Tag.class);

            query.setParameter("name", name);

            List<Tag> tag = query.list();

            if (tag.size() == 0) {
                throw new EntityNotFoundException("Tag", "id");
            }

            return tag.get(0);
        }
    }

    @Override
    public List<Post> getAllPostsByTagId(int tagId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery(
                    "SELECT p FROM Post p JOIN p.tags t WHERE t.id = :tagId", Post.class);

            query.setParameter("tagId", tagId);

            List<Post> result = query.getResultList();

            if (result.isEmpty()) {
                throw new EntityNotFoundException("Posts", "tagId", String.valueOf(tagId));
            }

            return result;
        }
    }

    @Override
    public List<Post> getAllPostsByTagName(String tagName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery(
                    "SELECT p FROM Post p JOIN p.tags t WHERE t.name = :tagName", Post.class);

            query.setParameter("tagName", tagName);

            List<Post> result = query.getResultList();

            if (result.size() == 0) {
                throw new EntityNotFoundException("Posts", "tag name");
            }

            return result;
        }
    }

    @Override
    public List<Tag> getAllTagsByPostId(int postId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Tag> query = session.createQuery(
                    "SELECT t FROM Post p JOIN p.tags t WHERE p.id = :postId", Tag.class);

            query.setParameter("postId", postId);

            List<Tag> result = query.getResultList();

            if (result.size() == 0) {
                throw new EntityNotFoundException("Tags", "post id");
            }

            return result;
        }
    }

    @Override
    public void createTagInPost(Tag tag, int postId, User user) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, postId);

            if (post == null) {
                throw new EntityNotFoundException("Post", postId);
            }

            Query<Tag> query = session.createQuery(
                    "SELECT t FROM Tag t WHERE t.name = :name", Tag.class);

            query.setParameter("name", tag.getName());

            Tag foundTag = query.getSingleResult();

            if (foundTag == null) {
                session.beginTransaction();
                session.persist(tag);
                session.getTransaction().commit();
            } else {
                post.getTags().add(tag);
                post.setCreatedBy(user);
                session.beginTransaction();
                session.merge(post);
                session.getTransaction().commit();
            }
        }
    }

    @Override
    public void updateTagInPost(Tag tag) {
//
//        try (Session session = sessionFactory.openSession()) {
//            Post post = session.get(Post.class, postId);
////
//            if (post != null) {
//                Tag tag = session.get(Tag.class, tagId);
//
//                if (tag != null) {
//                    session.beginTransaction();
//                    post.getTags().remove(tag);
//                    session.merge(post);
//                    session.getTransaction().commit();
////
//
//                }
//            }
////
//        }
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(tag);
            session.getTransaction().commit();
        }
    }


    @Override
    public void deleteTagInPost(int postId, int tagId) {

        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, postId);
//
            if (post != null) {
                Tag tag = session.get(Tag.class, tagId);

                if (tag != null) {
                    session.beginTransaction();
                    post.getTags().remove(tag);
                    session.merge(post);
                    session.getTransaction().commit();
//

                }
            }
//
    }}

    private String generateOrderBy(TagFilterOptions tagFilterOptions) {
        if (tagFilterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (tagFilterOptions.getSortBy().get()) {
            case "name":
                orderBy = "name";
                break;

        }

        orderBy = String.format(" order by %s", orderBy);

        if (tagFilterOptions.getSortOrder().isPresent() && tagFilterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }
}