package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Comment;
import com.example.forum.repositories.contracts.CommentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Comment> getAllCommentsByPostId(int postId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery("from Comment where post.id = :postId", Comment.class);
            query.setParameter("postId", postId);

            List<Comment> result = query.list();

            if (result.isEmpty()) {
                throw new EntityNotFoundException("Comment", "postId", String.valueOf(postId));
            }

            return result;
        }
    }

    @Override
    public Comment getCommentById(int commentId) {
        try (Session session = sessionFactory.openSession()) {
            Comment comment = session.get(Comment.class, commentId);

            if (comment == null) {
                throw new EntityNotFoundException("Comment", "id", String.valueOf(commentId));
            }

            return comment;
        }
    }

    @Override
    public void createComment(Comment comment, int postId) {

    }

    @Override
    public void updateComment(Comment comment, int postId) {

    }

    @Override
    public void deleteComment(int commentId) {
        Comment commentToDelete = getCommentById(commentId);

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(commentToDelete);
            session.getTransaction().commit();
        }
    }
}