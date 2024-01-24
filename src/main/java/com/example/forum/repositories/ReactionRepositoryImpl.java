package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.models.Reaction;
import com.example.forum.repositories.contracts.ReactionRepository;
import jakarta.persistence.SecondaryTable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
        return null;
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
    public Map<Reaction, Integer> countReactionsComment() {
        return null;
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