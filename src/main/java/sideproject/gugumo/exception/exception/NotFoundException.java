package sideproject.gugumo.exception.exception;

import sideproject.gugumo.response.StatusCode;

public class NotFoundException extends CustomException {
    public NotFoundException(StatusCode statusCode) {
        super(statusCode);
    }
}
