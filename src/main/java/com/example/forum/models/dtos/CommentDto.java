package com.example.forum.models.dtos;

import com.example.forum.models.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class CommentDto {

    @NotNull(message = "Comment content can't be empty")
    private String content;

    private User user;


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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}