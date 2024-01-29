package com.example.forum.models.enums;

public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private String name;

    Role(String name) {
        this.name = name;
    }
}