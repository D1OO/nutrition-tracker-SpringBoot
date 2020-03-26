package net.shvdy.sbproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.shvdy.sbproject.dto.FoodDTO;
import net.shvdy.sbproject.dto.FoodDTOContainer;
import net.shvdy.sbproject.dto.NewEntriesModalWindowDTO;
import net.shvdy.sbproject.entity.User;
import net.shvdy.sbproject.service.DailyRecordService;
import net.shvdy.sbproject.service.FoodService;
import net.shvdy.sbproject.service.exception.RecordDoesntExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * 21.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Controller
class FoodController {
    private final FoodService foodService;
    private final DailyRecordService dailyRecordService;
    private final ObjectMapper jacksonMapper;

    @ModelAttribute("newFoodDTO")
    public FoodDTO newUserDTO(@AuthenticationPrincipal User user) {
        return FoodDTO.builder().userId(user.getId()).build();
    }

    @Autowired
    public FoodController(FoodService foodService, DailyRecordService dailyRecordService, ObjectMapper jacksonMapper) {
        this.foodService = foodService;
        this.dailyRecordService = dailyRecordService;
        this.jacksonMapper = jacksonMapper;
    }

    @GetMapping(value = "/create-add-food-modal-window")
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
        return "fragments/user-page/add-food-modal-window/add-food :: content";
    }

    @PostMapping(value = "/")
    public String updateNewEntriesList(NewEntriesModalWindowDTO newEntriesWithNewChoice, Model model) {
        newEntriesWithNewChoice.getEntries().removeIf(x -> x.getFoodId() == null);
        FoodDTOContainer foodDTOContainer;
        try {
            foodDTOContainer = jacksonMapper.readValue(newEntriesWithNewChoice
                    .getFoodDTOContainerJSON(), FoodDTOContainer.class);
        } catch (IOException e) {
            foodDTOContainer = new FoodDTOContainer(Collections.emptyList());
            model.addAttribute("errors", "failed to get user's food from JSON string or it's empty");
        }
        model.addAttribute("newEntriesDTO", newEntriesWithNewChoice);
        model.addAttribute("userFood", foodService.convertListToHashMapOnFoodId(foodDTOContainer.getUserFood()));
        return "fragments/user-page/add-food-modal-window/add-food :: items2";
    }

    @PostMapping(value = "/save")
    public String saveNewEntriesList(NewEntriesModalWindowDTO newEntriesToSave) {
        Optional.of(newEntriesToSave).ifPresent(x -> {
            try {
                dailyRecordService.saveNewEntriesToRecordWithId(x.getRecordId(), x);
            } catch (RecordDoesntExistException e) {
                e.printStackTrace();
            }
        });
        return ("redirect:/");
    }

    @PostMapping(value = "/create-food")
    ResponseEntity<String> saveCreatedFood(@AuthenticationPrincipal User user, FoodDTO createdFood) {
        try {
            foodService.saveNewUsersFood(user.getId(), createdFood);
            return new ResponseEntity<>("Your age is ", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    "Year of birth cannot be in the future",
                    HttpStatus.BAD_REQUEST);
        }
    }
}