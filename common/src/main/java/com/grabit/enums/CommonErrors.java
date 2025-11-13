package com.grabit.enums;

import lombok.Getter;

public enum CommonErrors {
    unknown_error("Something Went Wrong"),
    AUTHENTICATION_FAILED("Error decoding signature"),
    AUTHENTICATION_EXPIRED("Signature has expired"),
    INVALID_REFRESH_TOKEN("Error decoding signature"),
    REFRESH_TOKEN_EXPIRED("Signature has expired"),
    Forbidden("You do not have the permission to access this resource"),
    REQUEST_BODY_MISSING("Request Body is required for this operation"),
    INVALID_MEMBER_ID("Member Id Provided is Not Valid");

    @Getter
    private String message;

    CommonErrors(String details) {
        this.message = details;
    }
}
