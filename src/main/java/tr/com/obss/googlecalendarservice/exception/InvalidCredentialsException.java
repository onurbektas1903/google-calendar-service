package tr.com.obss.googlecalendarservice.exception;

import tr.com.common.exceptions.BaseException;

import java.util.Set;

public class InvalidCredentialsException extends BaseException {
    private static final int code = 30001;
    private static final long serialVersionUID = 5002822407275812830L;

    public InvalidCredentialsException(String message) {
        super(message, code);
    }

    public InvalidCredentialsException(String message, Set<String> errList) {
        super(message, errList, code);
    }
}
