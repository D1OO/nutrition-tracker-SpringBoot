package net.shvdy.nutrition_tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shvdy.nutrition_tracker.dto.DailyRecordEntryDTO;
import net.shvdy.nutrition_tracker.dto.FoodDTO;
import net.shvdy.nutrition_tracker.dto.NewEntriesContainerDTO;
import net.shvdy.nutrition_tracker.entity.UserProfile;
import net.shvdy.nutrition_tracker.service.DailyRecordService;
import net.shvdy.nutrition_tracker.service.ServiceUtils;
import net.shvdy.nutrition_tracker.service.UserService;
import net.shvdy.nutrition_tracker.service.exception.NoValidProfileDataProvidedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

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
    private final ServiceUtils serviceUtils;
    private final SessionInfo sessionInfo;

    @Autowired
    public DiaryController(UserService userService, DailyRecordService dailyRecordService, ServiceUtils serviceUtils,
                           SessionInfo sessionInfo) {
        this.userService = userService;
        this.dailyRecordService = dailyRecordService;
        this.serviceUtils = serviceUtils;
        this.sessionInfo = sessionInfo;
    }

    @ModelAttribute("newFoodDTO")
    public FoodDTO newFoodDTO() {
        return new FoodDTO();
    }

    @RequestMapping("/food-diary")
    public String foodDiaryFragment(@RequestParam(name = "d", required = false) String date, Model model) {
        try {
            model.addAttribute("dailyCal", getDailyCalsNorm(sessionInfo.getUser().getUserProfile()));
        } catch (NoValidProfileDataProvidedException e) {
            return "fragments/user-page/complete-profile-to-proceed :: content";
        }
        model.addAttribute("paginatedRecords", dailyRecordService.getWeeklyRecords(
                sessionInfo.getUser().getUserProfile(),
                Optional.ofNullable(date).orElse(LocalDate.now().toString()),
                PageRequest.of(0, 7, Sort.Direction.DESC, "recordDate")));
        return "fragments/user-page/food-diary :: content";
    }

    @GetMapping(value = "/adding-entries-modal-window")
    public String createAddingEntriesWindow(@RequestParam Long recordId, @RequestParam String recordDate, Model model) {
        model.addAttribute("newEntriesDTO", NewEntriesContainerDTO.builder()
                .profileId(sessionInfo.getUser().getId())
                .recordId(recordId)
                .recordDate(recordDate)
                .entries(Collections.emptyList()).build());
        model.addAttribute("userFood",
                serviceUtils.FoodListEntityToDTO(sessionInfo.getUser().getUserProfile().getUserFood()));
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
    public String saveCreatedFood(FoodDTO createdFood) {
        sessionInfo.getUser().getUserProfile().getUserFood().add(serviceUtils.mapFoodDTOToEntity(createdFood));
        userService.updateUserProfile(sessionInfo.getUser().getUserProfile());
        return ("redirect:/");
    }

    private int getDailyCalsNorm(UserProfile userProfile) throws NoValidProfileDataProvidedException {
        if (userProfile.getLifestyle() == null)
            throw new NoValidProfileDataProvidedException();
        int i = (int) ((66 + 13.75 * userProfile.getWeight() + 5 * userProfile.getHeight()
                - 6.755 * userProfile.getAge()) * userProfile.getLifestyle().getFactor());
        if (i <= 0)
            throw new NoValidProfileDataProvidedException();
        return i;
    }

}