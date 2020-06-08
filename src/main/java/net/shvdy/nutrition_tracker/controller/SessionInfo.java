package net.shvdy.nutrition_tracker.controller;

import lombok.extern.log4j.Log4j2;
import net.shvdy.nutrition_tracker.model.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 04.06.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Log4j2
public class SessionInfo {

    private User user;

    public User getUser() {
        return Optional.ofNullable(user).orElseGet(() -> {
            this.user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user;
        });
    }

}
