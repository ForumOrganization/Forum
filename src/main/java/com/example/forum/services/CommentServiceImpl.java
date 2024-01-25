package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.exceptions.UnauthorizedOperationException;
import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.enums.Status;
import com.example.forum.repositories.contracts.CommentRepository;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.services.contracts.CommentService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.forum.utils.Messages.USER_HAS_BEEN_BLOCKED_OR_DELETED;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    private static final String MODIFY_ERROR_MESSAGE = "Only the creator of a comment can modify it.";

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository=postRepository;
    }

    @Override
    public List<Comment> getAllCommentsByPostId(int postId) {
        return this.commentRepository.getAllCommentsByPostId(postId);
    }

    @Override
    public void createComment(Comment comment, int postId, User user) {
        if(user.getStatus()==Status.BLOCKED || user.isDeleted()){
            throw new UnauthorizedOperationException(USER_HAS_BEEN_BLOCKED_OR_DELETED);
        }
        Post post=postRepository.getById(postId);
        if(post==null){
            throw new EntityNotFoundException("Post","id",String.valueOf(postId));
        }
        comment.setUser(user);
        comment.setPost(post);
        this.commentRepository.createComment(comment);
    }



    @Override
    public void updateComment(Comment comment, User user) {
        checkModifyPermission(comment.getId(), user);
        this.commentRepository.updateComment(comment);

    }



    @Override
    public void deleteComment(int commentId) {
        this.commentRepository.deleteComment(commentId);
    }

    private void checkModifyPermission(int commentId, User user) {
        Comment comment = this.commentRepository.getCommentById(commentId);

        if (comment.getUser().getId()!=user.getId()) {
            throw new AuthorizationException(MODIFY_ERROR_MESSAGE);
        }
    }
}