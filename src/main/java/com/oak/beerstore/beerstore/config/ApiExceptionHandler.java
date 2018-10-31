package com.oak.beerstore.beerstore.config;

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


    private static final String GENERAL_ERROR = "GENERAL_ERROR";
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

    private ApiError toApiError(String code, Locale locale) {
        try {
            return new ApiError(code, apiErrorMessageSource.getMessage(code, null, locale));
        } catch (NoSuchMessageException e) {
            LOG.error("Could not find any message for {} code under {} locale", code, locale);
            return generalError(locale);
        }
    }

    private ApiError generalError(Locale locale) {
        String message = apiErrorMessageSource.getMessage(GENERAL_ERROR, null, locale);
        return new ApiError(GENERAL_ERROR, message);
    }


}
