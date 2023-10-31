package ru.school.retailanalitycs_web_java.controllerAdvise;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.school.retailanalitycs_web_java.dto.ErrorDto;
import ru.school.retailanalitycs_web_java.exceptions.duplicateValue.DuplicateValueException;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.EntityNotFoundException;

import java.util.Collections;
import java.util.List;

import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.ENTITY_IS_NOT_VALID;
import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.SQL_GRAMMAR_ERROR;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorDto entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        log.warn("handle exception: EntityNotFoundException. Message: {}", ex.getMessage());
        return ErrorDto.builder()
                .code(ex.getCode())
                .messages(List.of(ex.getMessage()))
                .build();
    }

    @ExceptionHandler(DuplicateValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorDto duplicateValueExceptionHandler(DuplicateValueException ex) {
        log.warn("handle exception: DuplicateValueException. Message: {}", ex.getMessage());
        return ErrorDto.builder()
                .code(ex.getCode())
                .messages(List.of(ex.getMessage()))
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> messages = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        return ErrorDto.builder()
                .code(ENTITY_IS_NOT_VALID)
                .messages(messages)
                .build();
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleSQLGrammarException(BadSqlGrammarException exception) {
        log.warn("handle exception: SQLGrammarException. Message: {}", exception.getMessage());
        return ErrorDto.builder()
                .code(SQL_GRAMMAR_ERROR)
                .messages(Collections.singletonList(exception.getMessage()))
                .build();
    }

    @ExceptionHandler(PSQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleSQLGrammarException(PSQLException exception) {
        log.warn("handle exception: PSQLException. Message: {}", exception.getMessage());
        return ErrorDto.builder()
                .code(SQL_GRAMMAR_ERROR)
                .messages(Collections.singletonList(exception.getMessage()))
                .build();
    }
}
