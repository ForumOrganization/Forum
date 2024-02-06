package com.example.forum.exceptions;

public class EntityAlreadyDeleteException extends RuntimeException {
    public EntityAlreadyDeleteException(String type, String attribute, String value) {
        super(String.format("%s with %s %s has already been deleted.", type, attribute, value));
    }
}