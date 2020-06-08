package net.shvdy.nutrition_tracker.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Locale;

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
        return LocalDate.now()
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                        .withLocale(LocaleContextHolder.getLocale()));
    }

    @ModelAttribute("currentLocale")
    public Locale currentLocale() {
        return LocaleContextHolder.getLocale();
    }

    @ExceptionHandler
    public String serverError(final Exception e) throws Exception {
        e.printStackTrace();
        log.error("Exception: " + e);
        log.error("Exception: " + Arrays.toString(e.getStackTrace()));
        throw e;
    }
}