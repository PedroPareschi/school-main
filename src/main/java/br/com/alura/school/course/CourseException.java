package br.com.alura.school.course;

import br.com.alura.school.support.exception.SchoolException;
import org.springframework.http.HttpStatus;

public class CourseException extends SchoolException {

    public CourseException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
