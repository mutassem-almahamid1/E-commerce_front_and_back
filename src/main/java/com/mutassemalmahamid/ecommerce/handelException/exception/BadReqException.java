package com.mutassemalmahamid.ecommerce.handelException.exception;

import com.mutassemalmahamid.ecommerce.handelException.ApiBaseException;
import org.springframework.http.HttpStatus;

public class BadReqException extends ApiBaseException {

    public BadReqException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
