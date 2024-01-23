package com.example.forum.models.enums;

public enum Reaction {

    LIKES("LIKES"),
    DISLIKES("DISLIKES"),
    LOVE("LOVE");

    private String name;

    Reaction(String name) {
        this.name = name;
    }
}