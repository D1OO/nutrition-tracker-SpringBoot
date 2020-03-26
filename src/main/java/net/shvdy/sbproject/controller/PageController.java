package net.shvdy.sbproject.controller;

import net.shvdy.sbproject.dto.DailyRecordDTO;
import net.shvdy.sbproject.entity.User;
import net.shvdy.sbproject.service.DailyRecordService;
import net.shvdy.sbproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
public class PageController {
    private final DailyRecordService dailyRecordService;
    private final UserService userService;

    @Autowired
    public PageController(DailyRecordService dailyRecordService, UserService userService) {
        this.dailyRecordService = dailyRecordService;
        this.userService = userService;
    }

    @RequestMapping("/")
    public String mainPage(Authentication auth, @AuthenticationPrincipal User user, Model model) {
        if (auth != null && auth.isAuthenticated()) {
            model.addAttribute("user", user);
            return "user";
        } else
            return "index";
    }

    @RequestMapping("/admin")
    public String adminPage() {
        return "admin";
    }

    @RequestMapping("/food-diary")
    public String foodDiaryFragment(@AuthenticationPrincipal User user, Model model) {
        if (userService.getUserProfile(user.getId()).getDailyCalsNorm() > 0) {
            LocalDate currentDate = LocalDate.now();
            DailyRecordDTO dailyRecord = dailyRecordService.getForUserAndDate(user.getId(), currentDate.toString());
            model.addAttribute("localDate", currentDate);
            model.addAttribute("user", user);
            model.addAttribute("dailyRecord", dailyRecord);
            model.addAttribute("dailyCalsPercentage",
                    (int) (dailyRecord.getTotalCalories() / (double) user.getUserProfile().getDailyCalsNorm() * 100));
            return "fragments/user-page/food-diary :: content";
        } else return "fragments/user-page/complete-profile-to-proceed :: content";
    }


}