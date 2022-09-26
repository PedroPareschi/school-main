package br.com.alura.school.user;

import br.com.alura.school.support.exception.SchoolException;
import org.springframework.http.HttpStatus;

public class UserException extends SchoolException {
    public UserException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
