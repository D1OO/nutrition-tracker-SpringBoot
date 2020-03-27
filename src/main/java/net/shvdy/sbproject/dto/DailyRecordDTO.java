package net.shvdy.sbproject.dto;

import lombok.*;

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
public class DailyRecordDTO {

    private Long recordId;
    private Long profileId;
    private String recordDate;
    private List<DailyRecordEntryDTO> entries;
    private UserProfileDTO userProfile;

    public int getPercentage() {
        return entries == null ? 0 : (int) (getTotalCalories() / (double) userProfile.getDailyCalsNorm() * 100);
    }

    public int getTotalCalories() {
        return entries == null ? 0 : entries.stream()
                .mapToInt(x -> x.getFood().getCalories() * x.getQuantity() / 100).sum();
    }

    public int getTotalFats() {
        return entries == null ? 0 : entries.stream()
                .mapToInt(x -> x.getFood().getFats() * x.getQuantity() / 100).sum();
    }

    public int getTotalProteins() {
        return entries == null ? 0 : entries.stream()
                .mapToInt(x -> x.getFood().getProteins() * x.getQuantity() / 100).sum();
    }

    public int getTotalCarbs() {
        return entries == null ? 0 : entries.stream()
                .mapToInt(x -> x.getFood().getCarbohydrates() * x.getQuantity() / 100).sum();
    }
}
