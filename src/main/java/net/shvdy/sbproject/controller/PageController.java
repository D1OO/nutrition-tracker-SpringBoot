package net.shvdy.sbproject.controller;

import net.shvdy.sbproject.entity.User;
import net.shvdy.sbproject.service.DailyRecordService;
import net.shvdy.sbproject.service.UserService;
import net.shvdy.sbproject.service.exception.NoValidProfileDataProvidedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping("/food-diary")
    public String foodDiaryFragment(@RequestParam(name = "d", required = false) String date,
                                    @AuthenticationPrincipal User user, Model model) {
        try {
            user.getUserProfile().getDailyCalsNorm();
        } catch (NoValidProfileDataProvidedException e) {
            return "fragments/user-page/complete-profile-to-proceed :: content";
        }
        model.addAttribute("data", dailyRecordService.getPaginatedForUserAndLastDate(user.getUserProfile(),
                date, PageRequest.of(0, 7, Sort.Direction.DESC, "recordDate")));
        return "fragments/user-page/food-diary :: content";
    }

    @RequestMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getUsersList());
        return "admin";
    }
}