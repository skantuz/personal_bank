package info.skantuz.personal_bank.model.error;

import lombok.Getter;

/**
 * Custom exception class for API-related errors.
 * <p>
 * This exception encapsulates details about the error, including:
 * <ul>
 *   <li>Status code</li>
 *   <li>Error code</li>
 *   <li>Title</li>
 *   <li>Description</li>
 *   <li>Log message</li>
 * </ul>
 * </p>
 */
@Getter
public final class ApiException extends RuntimeException{
    private final int statusCode;
    private final String Code;
    private final String title;
    private final String description;
    private final String log;

    public ApiException(int statusCode, String code, String title, String description, String log) {
        super(description);
        this.statusCode = statusCode;
        Code = code;
        this.title = title;
        this.description = description;
        this.log = log;
    }

}
