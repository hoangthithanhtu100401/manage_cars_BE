package com.lixin.vehical_management.constants;


/**
 * A list of domain-specific status codes (along with human-readable messages)
 * to send back to user as part of a response.
 */
public enum CommonMessage {
    SUCCESS(0, "Success!"),
    FAILED(-1, "Failed!"),
    RESOURCE_NOT_FOUND(2, "Resource is not found!"),
    RESOURCE_EXISTED(3, "Resource has existed!"),
    INVALID_REQUEST(10, "Invalid request!"),
    UNAUTHORIZED(11, "Unauthorized!");

    public final int code;
    public final String message;

    CommonMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
