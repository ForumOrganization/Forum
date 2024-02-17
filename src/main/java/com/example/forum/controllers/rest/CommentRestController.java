package com.example.forum.controllers.rest;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.CommentMapper;
import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.dtos.CommentDto;
import com.example.forum.services.contracts.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.example.forum.utils.Messages.UNAUTHORIZED_USER_ERROR_MESSAGE;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService commentService;
    private final AuthenticationHelper authenticationHelper;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentRestController(CommentService commentService, AuthenticationHelper authenticationHelper, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.authenticationHelper = authenticationHelper;
        this.commentMapper = commentMapper;
    }

    @GetMapping("/post/{postId}")
    @Operation(tags ={"Retrieve all comments to a given post"},
            operationId = "post id to be searched for",
            summary = "This method retrieve information for all comments regarding a given post.",
            description = "This method search for all comments for one post. When a person is authorized and there are comments, a list with all comments for a particular post will be presented.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request body that accepts id as a parameters.",
                    content = @Content(schema = @Schema(implementation = Post.class))),
            parameters = {@Parameter( name = "postId", description = "path variable", example = "5")},
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Comment.class)), description = "Comment(s) was/were found successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Comment.class)), description = "You are not allowed to access the comment(s)."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Comment.class)), description = "Post with this id was not found.")})
    public List<Comment> getAllCommentsByPostId(@RequestHeader HttpHeaders headers, @PathVariable int postId,
                                                @RequestParam(required = false) String content,
                                                @RequestParam(required = false) Post post,
                                                @RequestParam(required = false) String sortBy,
                                                @RequestParam(required = false) String sortOrder) {
        //CommentFilterOptions commentFilterOptions = new CommentFilterOptions(post, content, sortBy, sortOrder);

        try {
            this.authenticationHelper.tryGetUser(headers);
            return commentService.getAllCommentsByPostId(postId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }

    @GetMapping("/{id}")
    @Operation(tags ={"Retrieve a comment"},
            operationId = "id to be searched for",
            summary = "This method search for a comment when comment id is given.",
            description = "This method search for a comment. A valid id must be given as an input.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request body that accepts comment id as a parameters.",
                    content = @Content(schema = @Schema(implementation = Comment.class))),
            parameters = {@Parameter( name = "postId", description = "path variable", example = "5")},
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Comment.class)), description = "The comment has been retrieved successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Comment.class)), description = "You are not allowed to access this comment."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Comment.class)), description = "Comment with this id was not found.")})
    public Comment getCommentById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(headers);
            return this.commentService.getCommentById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER_ERROR_MESSAGE);
        }
    }

    @PostMapping("/post/{postId}")
    @Operation(tags ={"Create a comment"},
            summary = "This method creates a comment to a given post when post id is given.",
            description = "This method creates a comment. A valid post id must be given as an input.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request that accepts post id parameters.",
                    content = @Content(schema = @Schema(implementation = Comment.class))),
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Comment.class)), description = "The comment has been found successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Comment.class)), description = "You are not allowed to see the comment."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Comment.class)), description = "Post with this id was not found.")})
    public Comment createComment(@RequestHeader HttpHeaders headers, @PathVariable int postId, @Valid @RequestBody CommentDto commentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = this.commentMapper.fromDto(commentDto);
            commentService.createComment(comment, postId, user);
            return comment;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(tags ={"Update a comment"},
            operationId = "Comment id to be updated",
            summary = "This method update a comment when id is given.",
            description = "This method update a comment. A valid object must be given as an input. An optional feature is that a tag can be attached to the post.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request body that accepts CommentDto as a parameter.",
                    content = @Content(schema = @Schema(implementation = Comment.class))),
            parameters = {@Parameter( name = "commentId", description = "path variable", example = "5")},
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Comment.class)), description = "The comment has been updated successfully"),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Comment.class)), description = "The comment with this id was not found."),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Comment.class)), description = "You are not allowed to modify this comment.")})
    public Comment updateComment(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody CommentDto commentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = this.commentMapper.fromDto(id, commentDto);
            commentService.updateComment(comment, user);
            return comment;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{commentId}")
    @Operation(tags ={"Delete a comment"},
            operationId = "id to be deleted",
            summary = "This method delete a comment when id is given.",
            description = "This method delete a comment. A valid object must be given as an input.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request that accepts comment id",
                    content = @Content(schema = @Schema(implementation = Comment.class))),
            parameters = {@Parameter( name = "commentId", description = "path variable", example = "5")},
            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Comment.class)), description = "The comment has been deleted successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Comment.class)), description = "You are not allowed to delete this comment."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Comment.class)), description = "The comment with this id was not found.")})
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