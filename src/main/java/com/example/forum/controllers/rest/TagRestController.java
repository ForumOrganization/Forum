package com.example.forum.controllers.rest;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.TagMapper;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.models.dtos.TagDto;
import com.example.forum.services.contracts.PostService;
import com.example.forum.services.contracts.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagRestController {

    private TagService tagService;
    private PostService postService;
    private AuthenticationHelper authenticationHelper;
    private TagMapper tagMapper;

    public TagRestController(TagService tagService, PostService postService,
                             AuthenticationHelper authenticationHelper,
                             TagMapper tagMapper) {
        this.tagService = tagService;
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
        this.tagMapper = tagMapper;
    }

    @GetMapping
    @Operation(tags = {"Get all tags"},
            summary = "This method retrieve information for all tags.",
            description = "This method search for all tags. When a person is authorized and there are valid tags, a list with all tags will be presented.",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Tag.class)), description = "Tag(s) were found successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Tag.class)), description = "You are not allowed to access the tag(s)."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Tag.class)), description = "Tag(s) is/were not found.")
            })
    public List<Tag> getAllTags() {
        return tagService.getAllTags();

    }

    @GetMapping("/{tagId}")
    @Operation(tags = {"Get a tag"},
            operationId = "id to be searched for",
            summary = "This method search for a tag when id is given.",
            description = "This method search for a tag. A valid id must be given as an input.",
            parameters = {@Parameter(name = "tagId", description = "path variable", example = "5")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Tag.class)), description = "The tag has been found successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Tag.class)), description = "You are not allowed to access this tag."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Tag.class)), description = "Tag with this id was not found.")})
    public Tag getTagById(@RequestHeader HttpHeaders headers, @PathVariable int tagId) {
        try {
            authenticationHelper.tryGetUser(headers);
            return tagService.getTagById(tagId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/search")
    @Operation(tags = {"Search for a tag"},
            summary = "This method search for a tag when tag name is given.",
            description = "This method aim to find a tag. A valid name must be given as an input.",
//
            parameters = {@Parameter(name = "tagName", description = "Tag name", example = "healthy food")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Tag.class)), description = "The tag has been updated successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Tag.class)), description = "You are not allowed to search for this tag."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Tag.class)), description = "Tag with this name was not found.")})
    public Tag getTagByName(@RequestHeader HttpHeaders headers, @RequestParam String name) {
        try {
            authenticationHelper.tryGetUser(headers);
            return tagService.getTagByName(name);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/posts/{postId}")
    @Operation(tags = {"Create a tag in post"},
            summary = "This method creates a tag in post when post id is given.",
            description = "This method creates a tag in post. A valid object must be given as an input.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request that accepts tag dto object as a parameters.",
                    content = @Content(schema = @Schema(implementation = Tag.class))),
            parameters = {@Parameter(name = "postId", description = "Post id", example = "healthy food")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Tag.class)), description = "The tag in this post has been created successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Tag.class)), description = "You are not allowed to create a tag in post."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Tag.class)), description = "The post with this id was not found.")})
    public ResponseEntity<Tag> createTagInPost(@PathVariable int postId, @Valid @RequestBody TagDto tagDto, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Tag tag = this.tagMapper.fromDto(tagDto);
            tagService.createTagInPost(tag, postId, user);
            return new ResponseEntity<>(tag, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/posts/{postId}/tag/{tagId}")
    @Operation(tags = {"Update a tag"},
            operationId = "id to be updated",
            summary = "This method update a tag when id is given.",
            description = "This method update a tag in a post. Valid post id and tag id must be given as an input.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request body that accepts TagDto parameters.",
                    content = @Content(schema = @Schema(implementation = Tag.class))),
            parameters = {@Parameter(name = "id", description = "post id/ path variable", example = "5"),
//                    @Parameter(name = "id", description = "tag id/ path variable", example = "1")
            },
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Tag.class)), description = "The tag has been updated successfully"),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Tag.class)), description = "You are not allowed to modify this tag."),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Tag.class)), description = "Tag with this id was not found.")})
    public ResponseEntity<Tag> updateTagInPost(@Valid @RequestBody TagDto tagDto, @PathVariable int postId, @PathVariable int tagId, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Tag tag = this.tagMapper.fromDto(tagId, tagDto);
            tagService.updateTagInPost(tag, user, postId, tagId);
            return new ResponseEntity<>(tag, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(tags = {"Delete a tag in a post"},
            operationId = "id to be deleted",
            summary = "This method delete a tag in a post when tag is given.",
            description = "This method delete a tag in a post. A valid tag object must be given as an input as well as post id.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request that accepts post id",
                    content = @Content(schema = @Schema(implementation = Tag.class))),
            parameters = {@Parameter(name = "id", description = "post id/ path variable", example = "5")},
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Tag.class)), description = "The post has been deleted successfully"),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Tag.class)), description = "The post with this id was not found."),
                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Tag.class)), description = "You are not allowed to delete this post.")})
    public ResponseEntity<Void> deleteTagInPost(@Valid @RequestBody TagDto tagDto, @PathVariable int postId, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post existingPost = postService.getById(postId);
            Tag tag = this.tagMapper.fromDto(tagDto);
            tagService.deleteTagInPost(tag,user, postId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}