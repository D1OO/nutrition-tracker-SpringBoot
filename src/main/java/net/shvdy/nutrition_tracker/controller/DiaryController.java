package net.shvdy.nutrition_tracker.controller;

import net.shvdy.nutrition_tracker.dto.DailyRecordDTO;
import net.shvdy.nutrition_tracker.dto.DailyRecordEntryDTO;
import net.shvdy.nutrition_tracker.dto.FoodDTO;
import net.shvdy.nutrition_tracker.dto.NewEntriesContainerDTO;
import net.shvdy.nutrition_tracker.model.service.DailyRecordService;
import net.shvdy.nutrition_tracker.model.service.Mapper;
import net.shvdy.nutrition_tracker.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
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

    @GetMapping("/food-diary/day")
    public String showDay(@RequestParam(name = "d", required = false) String day,
                          HttpServletRequest request, Model model) {
        model.addAttribute("showday", day);
        return recordsCache(request).stream().anyMatch(x -> x.getRecordDate().equals(day)) ?
                "fragments/user-page/food-diary :: content" : showWeek(day, request, model);
    }

    @GetMapping("/food-diary")
    public String showWeek(@RequestParam(name = "week", required = false) String date,
                           HttpServletRequest request, Model model) {
        int dailyNorm = dailyRecordService.getDailyCaloriesNorm(sessionInfo.getUser().getUserProfile());
        if (dailyNorm <= 0)
            return "fragments/user-page/complete-profile-to-proceed :: content";
        model.addAttribute("dailyNorm", dailyNorm);
        String dates = Optional.ofNullable(date).orElse(LocalDate.now().toString());
        request.getSession().setAttribute("weeklyRecords",
                dailyRecordService.getWeeklyRecords(sessionInfo.getUser().getUserProfile(),
                        dates, 7));
        model.addAttribute("showday", dates);
        return "fragments/user-page/food-diary :: content";
    }

    @GetMapping(value = "/food-diary/adding-entries-modal-window")
    public String createAddingEntriesWindow(@RequestParam String recordDate,
                                            @RequestParam int dailyCaloriesNorm, Model model) {
        model.addAttribute("newEntriesDTO", NewEntriesContainerDTO.builder()
                .recordDate(recordDate)
                .dailyCaloriesNorm(dailyCaloriesNorm)
                .entries(Collections.emptyList()).build());
        model.addAttribute("userFood", sessionInfo.getUser().getUserProfile().getUserFood()
                .stream().map(source -> Mapper.MODEL.map(source, FoodDTO.class)).collect(Collectors.toList()));
        return "fragments/user-page/add-entries-and-create-food :: content";
    }

    @PostMapping(value = "/food-diary/modal-window/added-entry")
    public String addedNewEntry(@RequestParam String foodDTOJSON, @RequestParam String foodName,
                                @RequestParam String newEntriesDTOJSON, Model model) throws IOException {
        NewEntriesContainerDTO newEntriesDTO = Mapper.JACKSON.readValue(newEntriesDTOJSON, NewEntriesContainerDTO.class);
        newEntriesDTO.getEntries().add(DailyRecordEntryDTO.builder().foodName(foodName).foodDTOJSON(foodDTOJSON).build());
        model.addAttribute("newEntriesDTO", newEntriesDTO);
        return "fragments/user-page/add-entries-and-create-food :: new-entries-list";
    }

    @PostMapping(value = "/food-diary/modal-window/removed-entry")
    public String removedNewEntry(@RequestParam int index, @RequestParam String newEntriesDTOJSON,
                                  Model model) throws IOException {
        NewEntriesContainerDTO newEntriesDTO = Mapper.JACKSON.readValue(newEntriesDTOJSON, NewEntriesContainerDTO.class);
        newEntriesDTO.getEntries().remove(index);
        model.addAttribute("newEntriesDTO", newEntriesDTO);
        return "fragments/user-page/add-entries-and-create-food :: new-entries-list";
    }

    @PostMapping(value = "/food-diary/modal-window/save-new-entries")
    public ResponseEntity<Void> saveNewEntriesList(@Valid NewEntriesContainerDTO newEntriesDTO, HttpServletRequest request) {
        DailyRecordDTO updatedRecord = Mapper.MODEL.map(dailyRecordService
                .saveNewEntries(newEntriesDTO, sessionInfo.getUser().getUserProfile()), DailyRecordDTO.class);
        Set<DailyRecordDTO> recordsCache = (Set<DailyRecordDTO>) request.getSession().getAttribute("weeklyRecords");
        recordsCache.remove(updatedRecord);
        recordsCache.add(updatedRecord);
        request.getSession().setAttribute("weeklyRecords", recordsCache);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/food-diary/modal-window/save-new-food")
    public String saveCreatedFood(@Valid FoodDTO createdFood) {
        sessionInfo.getUser()
                .setUserProfile(userService.saveCreatedFood(sessionInfo.getUser().getUserProfile(), createdFood));
        return ("/user");
    }

    private Set<DailyRecordDTO> recordsCache(HttpServletRequest request) {
        return (Set<DailyRecordDTO>) request.getSession().getAttribute("weeklyRecords");
    }

}