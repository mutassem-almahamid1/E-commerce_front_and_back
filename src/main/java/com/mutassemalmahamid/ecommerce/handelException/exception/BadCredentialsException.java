package com.mutassemalmahamid.ecommerce.handelException.exception;

import com.mutassemalmahamid.ecommerce.handelException.ApiBaseException;
import org.springframework.http.HttpStatus;

public class BadCredentialsException extends ApiBaseException {
    public BadCredentialsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.UNAUTHORIZED;
    }


}
