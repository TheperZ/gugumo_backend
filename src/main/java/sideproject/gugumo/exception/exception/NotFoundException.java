package sideproject.gugumo.exception.exception;

import sideproject.gugumo.response.StatusCode;

public class NotFoundException extends RuntimeException {
    public NotFoundException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
    }
}
