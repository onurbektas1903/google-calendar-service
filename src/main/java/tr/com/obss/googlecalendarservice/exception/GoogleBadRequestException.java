package tr.com.obss.googlecalendarservice.exception;

import tr.com.common.exceptions.BaseException;

import java.util.Set;

public class GoogleBadRequestException extends BaseException {
    private static final int code = 30002;
    private static final long serialVersionUID = 5002822407275812830L;

    public GoogleBadRequestException(String message) {
        super(message, code);
    }

    public GoogleBadRequestException(String message, Set<String> errList) {
        super(message, errList, code);
    }
}
