package com.example.forum.models.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class CommentDto {
    @NotNull(message = "Comment content can't be empty")
    @Size(min = 32, max = 8192, message = "The comment should be between 32 and 8192 symbols long.")
    private String content;

    public CommentDto(String content) {
        this.content = content;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}