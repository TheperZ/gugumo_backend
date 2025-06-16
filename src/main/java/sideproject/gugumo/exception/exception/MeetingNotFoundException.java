package sideproject.gugumo.exception.exception;

public class MeetingNotFoundException extends RuntimeException {

    public MeetingNotFoundException() {
    }

    public MeetingNotFoundException(String message) {
        super(message);
    }

    public MeetingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MeetingNotFoundException(Throwable cause) {
        super(cause);
    }
}
