package com.example.forum.models.dtos;

import jakarta.validation.constraints.Size;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public class PostDto {

    @NotNull(message = "Post title can't be empty")
    @Size(min = 16, max = 64, message = "Title should be between 16 and 64 symbols.")
    private String title;

    @NotNull(message = "Post content can't be empty")
    @Size(min = 32, max = 8192, message = "Content should be between 32 and 8192 symbols.")
    private String content;

    private UserDto createdBy;

    private LocalDate creationTime;

    private Set<CommentDto> comments;

    private Set<TagDto> tags;

    public PostDto() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserDto getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDto createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDate creationTime) {
        this.creationTime = creationTime;
    }

    public Set<CommentDto> getComments() {
        return comments;
    }

    public void setComments(Set<CommentDto> comments) {
        this.comments = comments;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public void setTags(Set<TagDto> tags) {
        this.tags = tags;
    }
}