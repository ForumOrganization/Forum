package com.example.forum.models.dtos;

import javax.validation.constraints.NotNull;

public class TagDto {

//    @NotNull(message = "Tag name can't be empty")
    private String name;

    public TagDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}