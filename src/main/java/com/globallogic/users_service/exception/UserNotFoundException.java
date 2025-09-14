package com.globallogic.users_service.exception;

/**
 * Exception thrown when a specific user cannot be found in the system.
 * This exception is typically used in cases where an operation depends
 * on the existence of a user, such as login or data retrieval, but the
 * user is not present in the database.
 *
 * This exception is commonly associated with HTTP 404 Not Found responses
 * and provides more clarity about the reason for failure in user-related
 * API operations.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
