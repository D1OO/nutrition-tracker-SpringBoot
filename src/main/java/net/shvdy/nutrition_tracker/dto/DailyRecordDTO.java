package net.shvdy.nutrition_tracker.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import net.shvdy.nutrition_tracker.model.service.Mapper;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 21.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class DailyRecordDTO {

    private Long recordId;
    private Long profileId;
    private String recordDate;
    private int dailyCaloriesNorm;
    private int percentage;
    private int totalCalories;
    private int totalFats;
    private int totalProteins;
    private int totalCarbs;
    private List<DailyRecordEntryDTO> entries = new ArrayList<>();
    private UserProfileDTO userProfile;

    public String name() {
        return LocalDate.parse(recordDate)
                .format(DateTimeFormatter.ofPattern("d EE", LocaleContextHolder.getLocale()));
    }

    @Override
    public String toString() {
        try {
            return Mapper.JACKSON.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException: " + e);
            return "";
        }
    }
}
