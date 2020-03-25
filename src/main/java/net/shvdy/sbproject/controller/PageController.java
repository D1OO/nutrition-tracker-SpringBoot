package net.shvdy.sbproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.shvdy.sbproject.dto.DailyRecordDTO;
import net.shvdy.sbproject.dto.FoodDTOContainer;
import net.shvdy.sbproject.dto.NewEntriesModalWindowDTO;
import net.shvdy.sbproject.entity.User;
import net.shvdy.sbproject.service.DailyRecordService;
import net.shvdy.sbproject.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Collections;

@Controller
public class PageController {
    private final DailyRecordService dailyRecordService;
    private final FoodService foodService;
    private final ObjectMapper jacksonMapper;

    @Autowired
    public PageController(DailyRecordService dailyRecordService, FoodService foodService, ObjectMapper jacksonMapper) {
        this.dailyRecordService = dailyRecordService;
        this.foodService = foodService;
        this.jacksonMapper = jacksonMapper;
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
    public String foodDiaryFragment(@AuthenticationPrincipal User user, Model model) {
        LocalDate currentDate = LocalDate.now();
        DailyRecordDTO dailyRecord = dailyRecordService.getForUserAndDate(user.getId(), currentDate.toString());
        model.addAttribute("localDate", currentDate);
        model.addAttribute("user", user);
        model.addAttribute("dailyRecord", dailyRecord);
        model.addAttribute("dailyCalsPercentage",
                (int) (dailyRecord.getTotalCalories() / (double) user.getUserProfile().getDailyCalsNorm() * 100));
        return "fragments/user-page/food-diary :: content";
    }

    @RequestMapping("/complete-profile-to-proceed")
    public String completeInfoToProceedFragment() {
        return "fragments/user-page/complete-profile-to-proceed :: content";
    }


    @PostMapping(value = "/create-add-food-modal-window")
    public String modalFragment(@RequestParam Long recordId, @RequestParam Long userId, Model model) {
        FoodDTOContainer userFoodContainer = new FoodDTOContainer(foodService.getUsersFood(userId));
        String FoodDTOContainerJSON = "";
        try {
            FoodDTOContainerJSON = jacksonMapper.writer().writeValueAsString(userFoodContainer);
        } catch (JsonProcessingException e) {
            model.addAttribute("errors", "failed to map user's FoodDTOContainer to JSON");
        }
        model.addAttribute("newEntriesDTO", new NewEntriesModalWindowDTO(recordId, FoodDTOContainerJSON, Collections.emptyList()));
        model.addAttribute("userFood", foodService.convertListToHashMapOnFoodId(userFoodContainer.getUserFood()));
        return "fragments/user-page/add-food-modal-window :: content";
    }

    @RequestMapping("/admin")
    public String adminPage() {
        return "admin";
    }
}