package com.example.forum.controllers;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.TagMapper;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.models.dtos.TagDto;
import com.example.forum.services.contracts.TagService;
import com.example.forum.utils.TagFilterOptions;
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
    private TagMapper tagMapper;

    public TagRestController(TagService tagService, AuthenticationHelper authenticationHelper, TagMapper tagMapper) {
        this.tagService = tagService;
        this.authenticationHelper = authenticationHelper;
        this.tagMapper = tagMapper;
    }

    @GetMapping
    public List<Tag> getAllTags(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        TagFilterOptions tagFilterOptions = new TagFilterOptions(name, sortBy, sortOrder);

        try {
            return tagService.getAllTags(tagFilterOptions);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{tagId}")
    public Tag getTagById(@PathVariable int tagId) {
        try {
            return tagService.getTagById(tagId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/search")
    public Tag getTagByName(@RequestParam String name) {
        try {
            return tagService.getTagByName(name);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/posts/{postId}")
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

    //TODO!!!
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deleteTagInPost(@Valid @RequestBody TagDto tagDto, @PathVariable int postId, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Tag tag = this.tagMapper.fromDto(tagDto);
            int tagId = tag.getId();
            tagService.deleteTagInPost(tag, user, postId, tagId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}