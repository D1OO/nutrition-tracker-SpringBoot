package net.shvdy.nutrition_tracker.controller;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * 28.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("currentDateLocalized")
    public String localisedDate() {
        return LocalDate.now()
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                        .withLocale(LocaleContextHolder.getLocale()));
    }

    @ModelAttribute("currentLocale")
    public Locale currentLocale() {
        return LocaleContextHolder.getLocale();
    }

//    @ExceptionHandler
//    public ResponseEntity<String> serverError(final Exception e) {
//        e.printStackTrace();
//        final String message = Optional.of(e.getMessage()).orElse(e.getClass().getSimpleName());
//        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}