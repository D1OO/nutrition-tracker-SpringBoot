package net.shvdy.nutrition_tracker.controller;

import net.shvdy.nutrition_tracker.dto.DailyRecordEntryDTO;
import net.shvdy.nutrition_tracker.dto.FoodDTO;
import net.shvdy.nutrition_tracker.dto.NewEntriesContainerDTO;
import net.shvdy.nutrition_tracker.service.DailyRecordService;
import net.shvdy.nutrition_tracker.service.Mapper;
import net.shvdy.nutrition_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 21.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Controller
class DiaryController {
    private final UserService userService;
    private final DailyRecordService dailyRecordService;
    private final SessionInfo sessionInfo;

    @Autowired
    public DiaryController(UserService userService, DailyRecordService dailyRecordService, SessionInfo sessionInfo) {
        this.userService = userService;
        this.dailyRecordService = dailyRecordService;
        this.sessionInfo = sessionInfo;
    }

    @ModelAttribute("newFoodDTO")
    public FoodDTO newFoodDTO() {
        return new FoodDTO();
    }

    @GetMapping("/food-diary")
    public String foodDiaryFragment(@RequestParam(name = "d", required = false) String date, Model model) {
        int dailyNorm = dailyRecordService.getDailyCaloriesNorm(sessionInfo.getUser().getUserProfile());
        if (dailyNorm <= 0)
            return "fragments/user-page/complete-profile-to-proceed :: content";
        model.addAttribute("dailyNorm", dailyNorm);
        model.addAttribute("paginatedRecords",
                dailyRecordService.getWeeklyRecords(sessionInfo.getUser().getUserProfile(),
                        Optional.ofNullable(date).orElse(LocalDate.now().toString()),
                        PageRequest.of(0, 7, Sort.Direction.DESC, "recordDate")));
        return "fragments/user-page/food-diary :: content";
    }

    @GetMapping(value = "/food-diary/adding-entries-modal-window")
    public String createAddingEntriesWindow(@RequestParam Long recordId, @RequestParam String recordDate,
                                            @RequestParam int dailyCaloriesNorm, Model model) {
        model.addAttribute("newEntriesDTO", NewEntriesContainerDTO.builder()
                .profileId(sessionInfo.getUser().getId())
                .recordId(recordId)
                .recordDate(recordDate)
                .dailyCaloriesNorm(dailyCaloriesNorm)
                .entries(Collections.emptyList()).build());
        model.addAttribute("userFood", sessionInfo.getUser().getUserProfile().getUserFood()
                .stream().map(source -> Mapper.MODEL.map(source, FoodDTO.class)).collect(Collectors.toList()));
        return "fragments/user-page/add-entries-and-create-food :: content";
    }

    @PostMapping(value = "/food-diary/modal-window/added-entry")
    public String updateAddingEntriesWindow(@RequestParam String foodDTOJSON, @RequestParam String foodName,
                                            @RequestParam String newEntriesDTOJSON, Model model) throws IOException {
        NewEntriesContainerDTO newEntriesDTO = Mapper.JACKSON.readValue(newEntriesDTOJSON, NewEntriesContainerDTO.class);
        newEntriesDTO.getEntries().add(DailyRecordEntryDTO.builder().foodName(foodName).foodDTOJSON(foodDTOJSON).build());
        model.addAttribute("newEntriesDTO", newEntriesDTO);
        return "fragments/user-page/add-entries-and-create-food :: new-entries-list";
    }

    @PostMapping(value = "/food-diary/modal-window/removed-entry")
    public String updateAddingEntriesWindow(@RequestParam int index, @RequestParam String newEntriesDTOJSON,
                                            Model model) throws IOException {
        NewEntriesContainerDTO newEntriesDTO = Mapper.JACKSON.readValue(newEntriesDTOJSON, NewEntriesContainerDTO.class);
        newEntriesDTO.getEntries().remove(index);
        model.addAttribute("newEntriesDTO", newEntriesDTO);
        return "fragments/user-page/add-entries-and-create-food :: new-entries-list";
    }

    @PostMapping(value = "/food-diary/modal-window/save-new-entries")
    public String saveNewEntriesList(NewEntriesContainerDTO newEntriesDTO) {
        if (!newEntriesDTO.getEntries().isEmpty())
            dailyRecordService.saveNewEntries(newEntriesDTO);
        return ("redirect:/");
    }

    @PostMapping(value = "/food-diary/modal-window/save-new-food")
    public String saveCreatedFood(FoodDTO createdFood) {
        sessionInfo.getUser()
                .setUserProfile(userService.saveCreatedFood(sessionInfo.getUser().getUserProfile(), createdFood));
        return ("redirect:/user");
    }

}