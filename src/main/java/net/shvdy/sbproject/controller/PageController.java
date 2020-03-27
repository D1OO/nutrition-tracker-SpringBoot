package net.shvdy.sbproject.controller;

import net.shvdy.sbproject.entity.User;
import net.shvdy.sbproject.service.DailyRecordService;
import net.shvdy.sbproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String foodDiaryFragment(@RequestParam(name = "d", required = false) String lastDate,
                                    @AuthenticationPrincipal User user, Model model) {
        if (userService.getUserProfile(user.getId()).getDailyCalsNorm() > 0) {
            if (lastDate == null) lastDate = LocalDate.now().toString();
            model.addAttribute("data", dailyRecordService
                    .getPaginatedForUserAndLastDate(user.getUserProfile(),
                            lastDate, PageRequest.of(0, 5, Sort.Direction.DESC, "recordDate")));
            LocalDate currentDate = LocalDate.now();
            model.addAttribute("localDate", currentDate);
            model.addAttribute("user", user);

            return "fragments/user-page/food-diary :: content";
        } else return "fragments/user-page/complete-profile-to-proceed :: content";
    }


}