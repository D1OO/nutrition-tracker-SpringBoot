package net.shvdy.sbproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shvdy.sbproject.dto.DailyRecordEntryDTO;
import net.shvdy.sbproject.dto.FoodDTO;
import net.shvdy.sbproject.dto.NewEntriesContainerDTO;
import net.shvdy.sbproject.entity.User;
import net.shvdy.sbproject.service.DailyRecordService;
import net.shvdy.sbproject.service.FoodService;
import net.shvdy.sbproject.service.FoodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;

/**
 * 21.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Controller
class DiaryController {
    private final FoodService foodService;
    private final DailyRecordService dailyRecordService;
    private final FoodUtils foodUtils;

    @Autowired
    public DiaryController(FoodService foodService, DailyRecordService dailyRecordService, FoodUtils foodUtils) {
        this.foodService = foodService;
        this.dailyRecordService = dailyRecordService;
        this.foodUtils = foodUtils;
    }

    @ModelAttribute("newFoodDTO")
    public FoodDTO newFoodDTO(@AuthenticationPrincipal User user) {
        return FoodDTO.builder().profileId(user.getUserProfile().getProfileId()).build();
    }

    @GetMapping(value = "/adding-entries-modal-window")
    public String createAddingEntriesWindow(@AuthenticationPrincipal User user, @RequestParam Long recordId,
                                            @RequestParam Long profileId, @RequestParam String recordDate, Model model) {
        model.addAttribute("newEntriesDTO", NewEntriesContainerDTO.builder()
                .profileId(profileId)
                .recordId(recordId)
                .recordDate(recordDate)
                .entries(Collections.emptyList()).build());
        model.addAttribute("userFood", foodUtils.entityToDTO(user.getUserProfile().getUserFood()));
        return "fragments/user-page/add-entries-and-create-food :: content";
    }

    @PostMapping(value = "/added-entry")
    public String updateAddingEntriesWindow(@RequestParam String foodDTOJSON, @RequestParam String foodName,
                                            @RequestParam String newEntriesDTOJSON, Model model) throws IOException {
        NewEntriesContainerDTO newEntriesDTO = new ObjectMapper().readValue(newEntriesDTOJSON, NewEntriesContainerDTO.class);
        newEntriesDTO.getEntries().add(DailyRecordEntryDTO.builder().foodName(foodName).foodDTOJSON(foodDTOJSON).build());
        model.addAttribute("newEntriesDTO", newEntriesDTO);
        return "fragments/user-page/add-entries-and-create-food :: new-entries-list";
    }

    @PostMapping(value = "/removed-entry")
    public String updateAddingEntriesWindow(@RequestParam int index, @RequestParam String newEntriesDTOJSON,
                                            Model model) throws IOException {
        NewEntriesContainerDTO newEntriesDTO = new ObjectMapper().readValue(newEntriesDTOJSON, NewEntriesContainerDTO.class);
        newEntriesDTO.getEntries().remove(index);
        model.addAttribute("newEntriesDTO", newEntriesDTO);
        return "fragments/user-page/add-entries-and-create-food :: new-entries-list";
    }

    @RequestMapping(value = "/save-new-entries")
    public String saveNewEntriesList(NewEntriesContainerDTO newEntriesDTO) throws IOException {
        if (!newEntriesDTO.getEntries().isEmpty())
            dailyRecordService.saveNewEntries(newEntriesDTO);
        return ("redirect:/");
    }

    @RequestMapping(value = "/save-new-food")
    public String saveCreatedFood(@AuthenticationPrincipal User user, FoodDTO createdFood) {
        user.getUserProfile().getUserFood().add(foodService.saveNewFood(createdFood));
        foodUtils.updateAuthenticatedUser(user);
        return ("redirect:/");
    }
}