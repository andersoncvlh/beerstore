package com.oak.beerstore.beerstore.config;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.oak.beerstore.beerstore.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.oak.beerstore.beerstore.error.ErrorResponse.ApiError;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

    private static final String MSG_NOT_FOUND = "error-2";
    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);
    private final MessageSource apiErrorMessageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, Locale locale) {
        Stream<ObjectError> errors = e.getBindingResult().getAllErrors().stream();

        List<ApiError> apiErrorList = errors
                .map(ObjectError::getDefaultMessage)
                .map(code -> toApiError(code, locale))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(ErrorResponse.of(HttpStatus.BAD_REQUEST, apiErrorList));
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException e, Locale locale) {
        final String errorCode = "general-invalid-format";
        final ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, toApiError(errorCode, locale, e.getValue()));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, Locale locale) {
        LOG.error("Error not expected", e);
        final String errorCode = "error-1";
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        final ErrorResponse errorResponse = ErrorResponse.of(internalServerError, toApiError(errorCode, locale));
        return ResponseEntity.status(internalServerError).body(errorResponse);
    }

    private ApiError toApiError(String code, Locale locale, Object... args) {
        try {
            return new ApiError(code, apiErrorMessageSource.getMessage(code, args, locale));
        } catch (NoSuchMessageException e) {
            return generalError(locale, code, locale.toString());
        }
    }

    private ApiError generalError(Locale locale, String... errorCode) {
        String message = apiErrorMessageSource.getMessage(MSG_NOT_FOUND, errorCode, locale);
        LOG.error(message);
        return new ApiError(MSG_NOT_FOUND, message);
    }


}
