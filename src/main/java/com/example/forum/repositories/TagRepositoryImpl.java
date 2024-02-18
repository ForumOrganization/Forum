package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.TagRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Tag> getAllTags() {
        try (Session session = sessionFactory.openSession()) {
            Query<Tag> query = session.createQuery("SELECT t from Tag t", Tag.class);
            return query.list();
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
                throw new EntityNotFoundException("Tag", "id", String.valueOf(tagId));
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

            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("Tag", "name", name);
            }

            return query.list().get(0);
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

            if (result.isEmpty()) {
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

            return query.getResultList();
        }
    }

    @Override
    public void createTagInPost(Tag tag, int postId, User user) {

        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, postId);
            Query<Tag> query = session.createQuery(
                    "SELECT t FROM Tag t WHERE t.name = :name", Tag.class);

            query.setParameter("name", tag.getName());

            List<Tag> foundTags = query.getResultList();

            if (foundTags.isEmpty()) {
                session.beginTransaction();
                session.persist(tag);
                session.getTransaction().commit();

                post.getTags().add(tag);
                session.beginTransaction();
                session.merge(post);
                session.getTransaction().commit();
            } else {
                Tag existingTag = foundTags.get(0);
                post.getTags().add(existingTag);

                session.beginTransaction();
                session.merge(post);
                session.getTransaction().commit();
            }
        }

    }

    @Override
    public void updateTagInPost(Tag tag) {
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

            if (post != null) {
                Tag tag = session.get(Tag.class, tagId);

                if (tag != null) {
                    post.getTags().remove(tag);
                    session.beginTransaction();
                    session.merge(post);
                    session.getTransaction().commit();
                }
            }
        }
    }

}