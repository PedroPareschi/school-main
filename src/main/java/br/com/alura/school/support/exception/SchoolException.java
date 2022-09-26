package br.com.alura.school.support.exception;

import org.springframework.http.HttpStatus;

public abstract class SchoolException extends Exception{

    private HttpStatus httpStatus;

    public SchoolException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
