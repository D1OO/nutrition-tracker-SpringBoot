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

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    public String showDay(@RequestParam(name = "d", required = false) String day, Model model) {
        model.addAttribute("showday", day);
        return sessionInfo.getDiaryCache().stream().anyMatch(x -> x.getRecordDate().equals(day)) ?
                "fragments/user-page/diary :: content" : showWeek(day, model);
    }

    @GetMapping("/food-diary")
    public String showWeek(@RequestParam(name = "week", required = false) String date, Model model) {
        if (List.of(sessionInfo.getUser().getUserProfile().getAge(),
                sessionInfo.getUser().getUserProfile().getHeight(),
                sessionInfo.getUser().getUserProfile().getWeight()).stream().anyMatch(n -> n == 0))
            return "fragments/user-page/complete-profile-to-proceed :: content";

        String periodLastDay = Optional.ofNullable(date).orElse(LocalDate.now().toString());
        sessionInfo.setDiaryCache(dailyRecordService.findByDatePeriod(sessionInfo.getUser().getUserProfile(),
                periodLastDay, 7));
        model.addAttribute("showday", periodLastDay);
        model.addAttribute("backDate", sessionInfo.getDiaryCache().first().getRecordDate()
                .equals(LocalDate.now().toString()) ? null : LocalDate.parse(sessionInfo.getDiaryCache().first()
                .getRecordDate()).plusDays(7).toString());
        return "fragments/user-page/diary :: content";
    }

    @GetMapping(value = "/food-diary/modal-window")
    public String createAddingEntriesWindow(@RequestParam String recordDate, Model model) {
        model.addAttribute("newEntriesDTO", NewEntriesContainerDTO.builder()
                .recordDate(recordDate).entries(new ArrayList<>()).build());
        return "fragments/user-page/diary-modal :: content";
    }

    @PostMapping(value = "/food-diary/modal-window/added-entry")
    public String addedNewEntry(@RequestParam String foodDTOJSON, @RequestParam String foodName,
                                @RequestParam String newEntriesDTOJSON, Model model) throws IOException {
        NewEntriesContainerDTO newEntriesDTO = Mapper.JACKSON.readValue(newEntriesDTOJSON, NewEntriesContainerDTO.class);
        newEntriesDTO.getEntries().add(DailyRecordEntryDTO.builder().foodName(foodName).foodDTOJSON(foodDTOJSON).build());
        model.addAttribute("newEntriesDTO", newEntriesDTO);
        return "fragments/user-page/diary-modal :: new-entries-list";
    }

    @PostMapping(value = "/food-diary/modal-window/removed-entry")
    public String removedNewEntry(@RequestParam int index, @RequestParam String newEntriesDTOJSON, Model model) throws IOException {
        NewEntriesContainerDTO newEntriesDTO = Mapper.JACKSON.readValue(newEntriesDTOJSON, NewEntriesContainerDTO.class);
        newEntriesDTO.getEntries().remove(index);
        model.addAttribute("newEntriesDTO", newEntriesDTO);
        return "fragments/user-page/diary-modal :: new-entries-list";
    }

    @PostMapping(value = "/food-diary/modal-window/save-new-entries")
    public ResponseEntity<Void> saveNewEntriesList(@Valid NewEntriesContainerDTO newEntriesDTO) {
        DailyRecordDTO updatedRecord = Mapper.MODEL.map(dailyRecordService
                .saveNewEntries(newEntriesDTO, sessionInfo.getUser().getUserProfile()), DailyRecordDTO.class);
        if (sessionInfo.getDiaryCache().remove(updatedRecord))
            sessionInfo.getDiaryCache().add(updatedRecord);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/food-diary/modal-window/save-new-food")
    public String saveCreatedFood(@Valid FoodDTO createdFood) {
        sessionInfo.getUser()
                .setUserProfile(userService.saveCreatedFood(sessionInfo.getUser().getUserProfile(), createdFood));
        return ("/user");
    }

    @PostMapping(value = "/food-diary/modal-window/updated-food")
    public String updatedFoodList() {
        return "/fragments/user-page/food-list";
    }

}