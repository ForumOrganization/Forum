package com.example.forum.exceptions;

public class UnauthorizedOperationException extends RuntimeException{
    public UnauthorizedOperationException(String message) {
        super(message);
    }

}
