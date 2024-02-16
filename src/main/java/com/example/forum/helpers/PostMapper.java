package com.example.forum.helpers;

import com.example.forum.models.Post;
import com.example.forum.models.Tag;
import com.example.forum.models.dtos.PostDto;
import com.example.forum.services.contracts.PostService;
import com.example.forum.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class PostMapper {

    private final PostService postService;
    private final TagService tagService;

    @Autowired
    public PostMapper(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    public Post fromDto(int id, PostDto dto) {
        Post post = fromDto(dto);
        post.setId(id);
        Post repositoryPost = postService.getById(id);
        post.setCreatedBy(repositoryPost.getCreatedBy());
        return post;
    }

    public Post fromDto(PostDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        return post;
    }

    public PostDto toDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setContent(post.getContent());
        postDto.setTitle(post.getTitle());
        return postDto;
    }
}