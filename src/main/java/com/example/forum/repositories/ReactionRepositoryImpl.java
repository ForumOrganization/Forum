package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.repositories.contracts.ReactionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class ReactionRepositoryImpl implements ReactionRepository {

    private SessionFactory sessionFactory;

    public ReactionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Reaction> getAllReactionsByPostId(Reaction reaction, int postId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Reaction> query = session.createQuery(
                    "SELECT r FROM Reaction r WHERE post.id = :postId", Reaction.class);

            query.setParameter("postId", postId);

            return query.getResultList();
        }
    }


    @Override
    public List<Reaction> getAllReactionsByCommentId(Reaction reaction, int commentId) {
        return null;
    }

    @Override
    public Map<Reaction, Integer> countReactionsPost(int postId) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, postId);

            if (post == null) {
                throw new EntityNotFoundException("Post", "postId", String.valueOf(postId));
            }

            Map<Reaction, Integer> reactionCountMap = new HashMap<>();

            Set<Reaction> reactions = post.getReactions();
            for (Reaction reaction : reactions) {
                reactionCountMap.put(reaction, reactionCountMap.getOrDefault(reaction, 0) + 1);
            }

            return reactionCountMap;
        }
    }

    @Override
    public Map<Reaction, Integer> countReactionsComment(int commentId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT r, COUNT(r) FROM Reaction r WHERE r.comment.id = :commentId GROUP BY r",
                    Object[].class);

            query.setParameter("commentId", commentId);

            List<Object[]> result = query.getResultList();

            Map<Reaction, Integer> reactionCountMap = new HashMap<>();
            for (Object[] row : result) {
                Reaction reaction = (Reaction) row[0];
                Long count = (Long) row[1];
                reactionCountMap.put(reaction, count.intValue());
            }

            return reactionCountMap;
        }
    }


    @Override
    public void updateReactionPost(Reaction reaction, int postId) {

    }

    @Override
    public void updateReactionComment(Reaction reaction, int commentId) {
        try (Session session = sessionFactory.openSession()) {
            Comment comment = session.get(Comment.class, commentId);

            if (comment == null) {
                throw new EntityNotFoundException("Comment", "commentId", String.valueOf(commentId));
            }

            Reaction existingReaction = findReactionByCommentIdAndUserId(commentId, reaction.getUser().getId());

            if (existingReaction != null) {
                existingReaction.setReaction(reaction.getReaction());
                session.merge(existingReaction);

            } else {
                reaction.setComment(comment);
                session.beginTransaction();
                session.persist(reaction);
                session.getTransaction().commit();
            }
        }
    }

    private Reaction findReactionByCommentIdAndUserId(int commentId, int userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Reaction where comment.id = :commentId and user.id = :userId", Reaction.class)
                    .setParameter("commentId", commentId)
                    .setParameter("userId", userId)
                    .uniqueResult();
        }
    }
}