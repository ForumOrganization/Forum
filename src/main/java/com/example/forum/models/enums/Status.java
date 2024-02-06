package com.example.forum.models.enums;

public enum Status {
    BLOCKED("BLOCKED"),
    ACTIVE("ACTIVE");

    private String name;

    Status(String name) {
        this.name = name;
    }
}