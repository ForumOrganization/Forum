package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.TagRepository;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public TagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Tag> getAllTags() {
        try (Session session = sessionFactory.openSession()) {
            Query<Tag> query = session.createQuery("from Tag", Tag.class);
            return query.list();
        }
    }

    @Override
    public List<Post> getAllPostsByTagId(int tagId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery(
                    "SELECT p FROM Post p JOIN p.tags t WHERE t.id = :tagId",
                    Post.class
            );

            query.setParameter("tagId", tagId);
            return query.getResultList();
        }
    }

    @Override
    public List<Post> getAllPostsByTagName(String tagName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery(
                    "SELECT p FROM Post p JOIN p.tags t WHERE t.name = :tagName",
                    Post.class
            );

            query.setParameter("tagName", tagName);
            return query.getResultList();
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
    public Tag getTagById(int tagId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Tag> query = session.createQuery(
                    "SELECT t FROM Tag t WHERE t.id = :tagId",
                    Tag.class
            );

            query.setParameter("tagId", tagId);

            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("Tag", tagId);
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
                    "SELECT t FROM Tag t WHERE t.name = :name",
                    Tag.class
            );

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
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(tag);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteTagInPost(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(tag);
            session.getTransaction().commit();
        }
    }
}