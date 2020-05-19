package net.shvdy.sbproject.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class NewEntriesContainerDTO {
    private Long recordId;
    private Long profileId;
    private String recordDate;
//    private String newEntriesContainerJSON;
    private List<DailyRecordEntryDTO> entries;

    @Override
    public String toString() {
        //Jackson (Java object to JSON String mapping)
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

//    private List<DailyRecordEntryDTO> getEntries(){
//        setNewEntriesContainerJSON(this.toString());
//    }
}
