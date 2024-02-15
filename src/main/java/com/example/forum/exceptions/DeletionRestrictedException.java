package com.example.forum.exceptions;

public class DeletionRestrictedException extends  RuntimeException{

        public DeletionRestrictedException(String message) {
            super(message);
        }


}
