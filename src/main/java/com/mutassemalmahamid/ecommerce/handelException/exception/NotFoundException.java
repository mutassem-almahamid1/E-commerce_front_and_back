package com.mutassemalmahamid.ecommerce.handelException.exception;

import com.mutassemalmahamid.ecommerce.handelException.ApiBaseException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiBaseException {
    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
