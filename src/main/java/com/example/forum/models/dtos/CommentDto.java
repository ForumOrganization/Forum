package com.example.forum.models.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class CommentDto {

    @NotNull(message = "Comment content can't be empty")
    @Size(min = 32, max = 8192, message = "The comment should be between 32 and 8192 symbols long.")
    private String content;

    private UserDto createdBy;
    private LocalDate creationTime;


    public CommentDto() {

    }

    public CommentDto(String content) {
        this.content = content;
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
}