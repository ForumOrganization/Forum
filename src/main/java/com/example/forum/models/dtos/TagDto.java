package com.example.forum.models.dtos;

import javax.validation.constraints.NotNull;

public class TagDto {
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