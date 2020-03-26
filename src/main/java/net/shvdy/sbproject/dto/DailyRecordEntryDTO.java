package net.shvdy.sbproject.dto;

import lombok.*;

/**
 * 20.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyRecordEntryDTO {
    private Long entryId;
    private Long foodId;
    private int quantity;
    private FoodDTO food;
    private DailyRecordDTO dailyRecordDTO;
}