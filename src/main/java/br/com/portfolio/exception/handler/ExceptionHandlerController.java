package br.com.portfolio.exception.handler;

import static java.util.stream.Collectors.toList;

import br.com.portfolio.domain.response.ErrorResponse;
import br.com.portfolio.exception.CustomerAlreadyExistsException;
import br.com.portfolio.exception.CustomerNotFoundException;
import java.util.Collection;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerController {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Collection<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(violation -> ErrorResponse.as(messageSource.getMessage(violation, LocaleContextHolder.getLocale()))
                        .tag(simpleKey(violation)))
                .collect(toList());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Collection<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        return exception.getConstraintViolations()
                .stream()
                .map(violation -> ErrorResponse.as(violation.getMessage()).tag(violation.getPropertyPath().toString()))
                .collect(toList());
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResponse handlePCustomerNotFoundException(CustomerNotFoundException exception) {
        return exceptionMessage(exception);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ErrorResponse handlePriceAlreadyExistsException(CustomerAlreadyExistsException exception) {
        return exceptionMessage(exception);
    }

    private ErrorResponse exceptionMessage(Throwable throwable, Object... params) {
        return ErrorResponse.as(message(throwable.getClass().getSimpleName().concat(".message"), params));
    }

    private String simpleKey(ObjectError violation) {
        return violation instanceof FieldError ? ((FieldError) violation).getField() : violation.getObjectName();
    }

    private String message(String code, Object... params) {
        return messageSource.getMessage(code, params, LocaleContextHolder.getLocale());
    }
}
