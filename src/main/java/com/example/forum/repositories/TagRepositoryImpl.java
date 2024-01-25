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
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private SessionFactory sessionFactory;

    public TagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Tag> getAllTags() {
        return null;
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
        return null;
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

            post.getTags().add(tag);

            post.setCreatedBy(user);

            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updateTagInPost(int tagId) {

    }

    @Override
    public void deleteTagInPost(int tagId) {

    }
}