package br.com.alura.school.enrollments;

import br.com.alura.school.support.exception.SchoolException;
import org.springframework.http.HttpStatus;

public class EnrollmentException extends SchoolException {

    public EnrollmentException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
