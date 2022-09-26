package br.com.alura.school.section;

import br.com.alura.school.support.exception.SchoolException;
import org.springframework.http.HttpStatus;

public class SectionException extends SchoolException{

    public SectionException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
