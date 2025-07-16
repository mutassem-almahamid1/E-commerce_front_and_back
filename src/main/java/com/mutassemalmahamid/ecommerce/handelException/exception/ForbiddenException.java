package com.mutassemalmahamid.ecommerce.handelException.exception;

import com.mutassemalmahamid.ecommerce.handelException.ApiBaseException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends ApiBaseException {
    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.FORBIDDEN;
    }
}