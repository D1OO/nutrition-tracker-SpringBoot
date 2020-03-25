package net.shvdy.sbproject.dto;

import lombok.*;

import java.util.List;

/**
 * 23.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEntriesModalWindowDTO {
    private Long recordId;
    private String foodDTOContainerJSON;
    private List<DailyRecordEntryDTO> entries;
}
