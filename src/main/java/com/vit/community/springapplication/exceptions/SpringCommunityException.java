package com.vit.community.springapplication.exceptions;

public class SpringCommunityException extends RuntimeException {
    public SpringCommunityException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public SpringCommunityException(String exMessage) {
        super(exMessage);
    }
}
