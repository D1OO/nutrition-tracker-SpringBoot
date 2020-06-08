package net.shvdy.nutrition_tracker.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 05.06.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Controller
public class ErrorControllerImpl implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        int statusCode = (int) Optional.ofNullable(request
                .getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).orElse(500);

        model.addAttribute("code", statusCode);
        model.addAttribute("error", HttpStatus.valueOf(statusCode).getReasonPhrase());
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}

