package sideproject.gugumo.exception.exception;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException() {
        super();
    }

    public NotificationNotFoundException(String message) {
        super(message);
    }

    public NotificationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
