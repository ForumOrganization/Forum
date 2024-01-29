package com.example.forum.controllers;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.services.contracts.TagService;
import com.example.forum.utils.TagFilterOptions;
import jakarta.persistence.NoResultException;
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
    private AuthenticationHelper authenticationHelper;

    public TagRestController(TagService tagService, AuthenticationHelper authenticationHelper) {

        this.tagService = tagService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Tag> getAllTags(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        TagFilterOptions tagFilterOptions = new TagFilterOptions(name, sortBy, sortOrder);
        return tagService.getAllTags(tagFilterOptions);
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<Tag> getTagById(@PathVariable int tagId) {
        Tag tag = tagService.getTagById(tagId);
        if (tag != null) {
            return new ResponseEntity<>(tag, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Tag> getTagByName(@RequestParam String name) {
        Tag tag = tagService.getTagByName(name);
        if (tag != null) {
            return new ResponseEntity<>(tag, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/posts/{postId}")
    public List<Tag> getAllTagsByPostId(@PathVariable int postId) {
        try{
            return tagService.getAllTagsByPostId(postId);
        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/posts/tagName/{tagName}")
    public List<Post> getAllPostsByTagName(@PathVariable String tagName) {
        return tagService.getAllPostsByTagName(tagName);
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<Tag> createTagInPost(@PathVariable int postId, @Valid @RequestBody Tag tag, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            tagService.createTagInPost(tag, postId, user);
            return new ResponseEntity<>(tag, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/posts")
    public ResponseEntity<Tag> updateTagInPost(@Valid @RequestBody Tag tag, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            tagService.updateTagInPost(tag, user);
            return new ResponseEntity<>(tag, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/posts/{tagId}")
    public ResponseEntity<Void> deleteTagInPost(@PathVariable int tagId, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            tagService.deleteTagInPost(tagId, user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}