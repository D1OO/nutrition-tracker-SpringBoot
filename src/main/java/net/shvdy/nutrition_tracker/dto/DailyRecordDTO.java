package net.shvdy.nutrition_tracker.dto;

import lombok.*;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyRecordDTO that = (DailyRecordDTO) o;
        return getRecordDate().equals(that.getRecordDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecordDate());
    }
}
