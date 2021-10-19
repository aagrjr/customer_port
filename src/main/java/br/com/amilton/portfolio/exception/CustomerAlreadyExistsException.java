package br.com.amilton.portfolio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class CustomerAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 9138294891674773224L;

}
