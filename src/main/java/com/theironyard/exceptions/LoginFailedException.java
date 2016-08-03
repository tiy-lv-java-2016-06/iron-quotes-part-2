package com.theironyard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by EddyJ on 8/2/16.
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Login Failed")
public class LoginFailedException extends RuntimeException {
}