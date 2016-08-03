package com.theironyard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by jeff on 8/2/16.
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Token has expired")
public class TokenExpiredException extends RuntimeException {
}
