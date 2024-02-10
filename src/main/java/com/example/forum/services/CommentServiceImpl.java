package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.enums.Reaction;
import com.example.forum.models.enums.Status;
import com.example.forum.repositories.contracts.CommentRepository;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.services.contracts.CommentService;
import com.example.forum.utils.CommentFilterOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.forum.utils.CheckPermission.checkAccessPermissions;
import static com.example.forum.utils.CheckPermission.checkAccessPermissionsUser;
import static com.example.forum.utils.Messages.*;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Comment> getAllCommentsByPostId(int postId) {
        return this.commentRepository.getAllCommentsByPostId(postId);
    }

    @Override
    public Comment getCommentById(int commentId) {
        return this.commentRepository.getCommentById(commentId);
    }

    @Override
    public void createComment(Comment comment, int postId, User user) {
        checkBlockOrDeleteUser(user);

        Post post = postRepository.getById(postId);

        comment.setUser(user);
        comment.setPost(post);

        this.commentRepository.createComment(comment);
    }

    @Override
    public void updateComment(Comment comment, User user) {
        checkBlockOrDeleteUser(user);
        //Comment commentToUpdate = commentRepository.getCommentById(comment.getId());
        checkAccessPermissionsUser(comment.getUser().getId(), user, UPDATE_USER_MESSAGE_ERROR);
        this.commentRepository.updateComment(comment);
    }

    @Override
    public void deleteComment(int commentId, User user) {
        checkBlockOrDeleteUser(user);
        Comment comment = commentRepository.getCommentById(commentId);
        checkAccessPermissions(comment.getUser().getId(), user, MODIFY_POST_ERROR_MESSAGE);
        this.commentRepository.deleteComment(commentId, user);
    }

    private static void checkBlockOrDeleteUser(User user) {
        if (user.getStatus() == Status.BLOCKED || user.isDeleted()) {
            throw new AuthorizationException(USER_HAS_BEEN_BLOCKED_OR_DELETED);
        }
    }
}