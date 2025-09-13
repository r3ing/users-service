package com.globallogic.users_service.exception;

/**
 * Exception thrown when attempting to create a user that already exists in the system.
 * Typically used to signal conflicts in user registration or creation processes.
 *
 * This exception is commonly associated with HTTP 409 Conflict responses and is intended
 * to provide clear feedback when a duplicate user is detected, often based on unique
 * identifiers such as email addresses or usernames.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
