package sideproject.gugumo.exception.exception;

import sideproject.gugumo.response.StatusCode;

public class DuplicateResourceException extends CustomException {
    public DuplicateResourceException(StatusCode statusCode) {
        super(statusCode);
    }
}
