package net.shvdy.sbproject.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;

/**
 * 28.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("currentDate")
    public LocalDate currentDate() {
        return LocalDate.now();
    }
}