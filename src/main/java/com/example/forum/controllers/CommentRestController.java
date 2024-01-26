package com.example.forum.controllers;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.models.Comment;
import com.example.forum.models.User;
import com.example.forum.services.contracts.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService commentService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CommentRestController(CommentService commentService, AuthenticationHelper authenticationHelper) {
        this.commentService = commentService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/post/{postId}")
    public List<Comment> getAllCommentsByPostId(@PathVariable int postId) {
        return commentService.getAllCommentsByPostId(postId);
    }

    @PostMapping("/post/{postId}")
    public Comment createComment(@RequestHeader HttpHeaders headers, @PathVariable int postId, @Valid @RequestBody Comment comment) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            commentService.createComment(comment, postId, user);
            return comment;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping
    public Comment updateComment(@RequestHeader HttpHeaders headers, @Valid @RequestBody Comment comment) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            commentService.updateComment(comment, user);
            return comment;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@RequestHeader HttpHeaders headers, @PathVariable int commentId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            commentService.deleteComment(commentId, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}