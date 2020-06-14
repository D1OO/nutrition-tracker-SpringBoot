package net.shvdy.nutrition_tracker.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 28.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@ControllerAdvice
@Log4j2
public class GlobalControllerAdvice {

    @ModelAttribute("currentDateLocalized")
    public String localisedDate() {
        return LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                        .withLocale(LocaleContextHolder.getLocale()));
    }

    @ModelAttribute("currentLocale")
    public Locale currentLocale() {
        return LocaleContextHolder.getLocale();
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(BindException ex) {
        return new ResponseEntity<>(ex.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(x -> ((FieldError) x).getField() + "Error",
                        y -> Optional.ofNullable(y.getDefaultMessage()).orElse(""))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public String serverError(final Exception e) throws Exception {
        e.printStackTrace();
        log.error("Exception: " + e);
        log.error("Exception: " + Arrays.toString(e.getStackTrace()));
        throw e;
    }
}