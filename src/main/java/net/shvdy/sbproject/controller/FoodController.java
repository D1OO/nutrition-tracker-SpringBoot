package net.shvdy.sbproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shvdy.sbproject.dto.FoodDTOContainer;
import net.shvdy.sbproject.dto.NewEntriesModalWindowDTO;
import net.shvdy.sbproject.service.DailyRecordService;
import net.shvdy.sbproject.service.FoodService;
import net.shvdy.sbproject.service.exception.RecordDoesntExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

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

    @Autowired
    public FoodController(FoodService foodService, DailyRecordService dailyRecordService, ObjectMapper jacksonMapper) {
        this.foodService = foodService;
        this.dailyRecordService = dailyRecordService;
        this.jacksonMapper = jacksonMapper;
    }

    @PostMapping(value = "/")
    public String updateNewEntriesList(NewEntriesModalWindowDTO newEntriesWithNewChoice, Model model) {
        FoodDTOContainer as;
        try {
            as = jacksonMapper.readValue(newEntriesWithNewChoice.getFoodDTOContainerJSON(), FoodDTOContainer.class);
        } catch (IOException e) {
            as = new FoodDTOContainer(Collections.emptyList());
            model.addAttribute("errors", "failed to get user's food from JSON string or it's empty");
        }
        model.addAttribute("newEntriesDTO", newEntriesWithNewChoice);
        model.addAttribute("userFood", foodService.convertListToHashMapOnFoodId(as.getUserFood()));
        return "fragments/user-page/add-food-modal-window :: items2";
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
}