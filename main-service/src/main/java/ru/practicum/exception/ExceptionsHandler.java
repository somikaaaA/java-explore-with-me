package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.dto.ApiError;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Collections;

@RestControllerAdvice
@Slf4j
public class ExceptionsHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        log.error("Ошибка валидации параметра: {}", e.getMessage(), e);
        return new ApiError(
                Collections.singletonList(stackTrace),
                "Ошибка валидации параметра",
                e.getMessage(),
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        log.error("Нарушение целостности данных: {}", e.getMessage(), e);
        return new ApiError(
                Collections.singletonList(stackTrace),
                e.getMessage(),
                "Нарушение целостности данных",
                HttpStatus.CONFLICT.toString(),
                LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleDataNotFoundException(DataNotFoundException e) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        log.error("Данные не найдены: {}", e.getMessage(), e);
        return new ApiError(
                Collections.singletonList(stackTrace),
                e.getMessage(),
                "Данные не найдены",
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataConflictException(DataConflictException e) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        log.error("Конфликт данных: {}", e.getMessage(), e);
        return new ApiError(
                Collections.singletonList(stackTrace),
                e.getMessage(),
                "Данные не удовлетворяют правилам",
                HttpStatus.CONFLICT.toString(),
                LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleDataBadRequestException(DataBadRequestException e) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        log.error("Некорректный запрос: {}", e.getMessage(), e);
        return new ApiError(
                Collections.singletonList(stackTrace),
                e.getMessage(),
                "Запрос составлен некорректно",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        log.error("Отсутствует обязательный параметр запроса: {}", e.getMessage(), e);
        return new ApiError(
                Collections.singletonList(stackTrace),
                e.getMessage(),
                "Отсутствует обязательный параметр запроса",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnknownException(Throwable e) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        log.error("Необрабатываемая ошибка: {}", e.getMessage(), e);
        return new ApiError(
                Collections.singletonList(stackTrace),
                e.getMessage(),
                "Необрабатываемая ошибка",
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                LocalDateTime.now().toString()
        );
    }
}