package net.shvdy.nutrition_tracker.controller;

import lombok.extern.log4j.Log4j2;
import net.shvdy.nutrition_tracker.dto.DailyRecordDTO;
import net.shvdy.nutrition_tracker.model.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.TreeSet;

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
    private TreeSet<DailyRecordDTO> diaryCache;

    public User getUser() {
        return Optional.ofNullable(user).orElseGet(() -> {
            this.user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user;
        });
    }

    public TreeSet<DailyRecordDTO> getDiaryCache() {
        return diaryCache;
    }

    public void setDiaryCache(TreeSet<DailyRecordDTO> diaryCache) {
        this.diaryCache = diaryCache;
    }

    public String getNextWeekDate() {
        return LocalDate.parse(diaryCache.last().getRecordDate()).minusDays(1).toString();
    }

}
