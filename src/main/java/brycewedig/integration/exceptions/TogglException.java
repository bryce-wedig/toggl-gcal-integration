package brycewedig.integration.exceptions;

public class TogglException extends Exception {

    public TogglException(int httpStatusCode) {
        super("Request failed with HTTP Status Code " + httpStatusCode);
    }

    public TogglException(String message) {
        super(message);
    }

    public TogglException(String message, Throwable cause) { super(message, cause); }
}
