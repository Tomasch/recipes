package com.kitchen.exception;

public class InvalidRecipeTypeException extends RuntimeException {
    public InvalidRecipeTypeException(String message) {
        super(message);
    }
}
