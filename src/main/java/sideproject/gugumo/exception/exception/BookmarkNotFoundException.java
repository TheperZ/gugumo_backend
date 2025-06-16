package sideproject.gugumo.exception.exception;

public class BookmarkNotFoundException extends RuntimeException {

    public BookmarkNotFoundException() {
    }

    public BookmarkNotFoundException(String message) {
        super(message);
    }

    public BookmarkNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookmarkNotFoundException(Throwable cause) {
        super(cause);
    }
}
