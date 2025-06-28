package sideproject.gugumo.exception.exception;

import sideproject.gugumo.response.StatusCode;

public class CustomServerError extends CustomException {
    public CustomServerError(StatusCode statusCode) {
        super(statusCode);
    }
}
