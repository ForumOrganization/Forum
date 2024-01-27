package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.models.Reaction_comments;
import com.example.forum.models.Reaction_posts;
import com.example.forum.repositories.contracts.ReactionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class ReactionRepositoryImpl implements ReactionRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public ReactionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Reaction_posts> getAllReactionsByPostId(int postId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Reaction_posts> query = session.createQuery(
                    "SELECT r FROM Reaction_posts r WHERE post.id = :postId", Reaction_posts.class);

            query.setParameter("postId", postId);

            return query.getResultList();
        }
    }

    @Override
    public List<Reaction_comments> getAllReactionsByCommentId(int commentId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Reaction_comments> query = session.createQuery(
                    "SELECT r FROM Reaction_comments r WHERE comment.id = :commentId", Reaction_comments.class);

            query.setParameter("commentId", commentId);

            return query.getResultList();
        }
    }

    @Override
    public Map<Reaction_posts, Integer> countReactionsPost(int postId) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, postId);

            if (post == null) {
                throw new EntityNotFoundException("Post", "postId", String.valueOf(postId));
            }

            Map<Reaction_posts, Integer> reactionCountMap = new HashMap<>();

            Set<Reaction_posts> reactions = post.getReactions();
            for (Reaction_posts reaction : reactions) {
                reactionCountMap.put(reaction, reactionCountMap.getOrDefault(reaction, 0) + 1);
            }

            return reactionCountMap;
        }
    }

    @Override
    public Map<Reaction_comments, Integer> countReactionsComment(int commentId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT r, COUNT(r) FROM Reaction_comments r WHERE r.comment.id = :commentId GROUP BY r",
                    Object[].class);

            query.setParameter("commentId", commentId);

            List<Object[]> result = query.getResultList();

            Map<Reaction_comments, Integer> reactionCountMap = new HashMap<>();
            for (Object[] row : result) {
                Reaction_comments reaction = (Reaction_comments) row[0];
                Long count = (Long) row[1];
                reactionCountMap.put(reaction, count.intValue());
            }

            return reactionCountMap;
        }
    }

    @Override
    public void updateReactionPost(Reaction_posts reaction, int postId) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, postId);

            if (post == null) {
                throw new EntityNotFoundException("Post", "postId", String.valueOf(postId));
            }

            Reaction_posts existingReaction = findReactionByPostIdAndUserId(postId, reaction.getUser().getId());

            if (existingReaction != null) {
                existingReaction.setReaction(reaction.getReaction());
                session.merge(existingReaction);

            } else {
                reaction.setPost(post);
                session.beginTransaction();
                session.persist(reaction);
                session.getTransaction().commit();
            }
        }
    }

    @Override
    public void updateReactionComment(Reaction_comments reaction, int commentId) {
        try (Session session = sessionFactory.openSession()) {
            Comment comment = session.get(Comment.class, commentId);

            if (comment == null) {
                throw new EntityNotFoundException("Comment", "commentId", String.valueOf(commentId));
            }

            Reaction_comments existingReaction = findReactionByCommentIdAndUserId(commentId, reaction.getUser().getId());

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

    private Reaction_comments findReactionByCommentIdAndUserId(int commentId, int userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Reaction_comments where comment.id = :commentId and user.id = :userId", Reaction_comments.class)
                    .setParameter("commentId", commentId)
                    .setParameter("userId", userId)
                    .uniqueResult();
        }
    }

    private Reaction_posts findReactionByPostIdAndUserId(int postId, int userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Reaction_posts where post.id = :postId and user.id = :userId", Reaction_posts.class)
                    .setParameter("postId", postId)
                    .setParameter("userId", userId)
                    .uniqueResult();
        }
    }
}