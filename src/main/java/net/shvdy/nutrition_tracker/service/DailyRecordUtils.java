package net.shvdy.nutrition_tracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shvdy.nutrition_tracker.dto.DailyRecordEntryDTO;
import net.shvdy.nutrition_tracker.dto.NewEntriesContainerDTO;
import net.shvdy.nutrition_tracker.entity.DailyRecord;
import net.shvdy.nutrition_tracker.entity.DailyRecordEntry;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 29.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Service
public class DailyRecordUtils {
    private ObjectMapper jacksonMapper;
    private ServiceUtils serviceUtils;
    @PersistenceContext
    private EntityManager entityManager;

    public DailyRecordUtils(ServiceUtils serviceUtils, ObjectMapper jacksonMapper) {
        this.serviceUtils = serviceUtils;
        this.jacksonMapper = jacksonMapper;
    }

    public NewEntriesContainerDTO getNewEntriesDTOFromJSON(String newEntriesDTOJSON) throws IOException {
        return jacksonMapper.readValue(newEntriesDTOJSON, NewEntriesContainerDTO.class);
    }

    public List<DailyRecordEntry> mapDTOtoEntity(DailyRecord record, NewEntriesContainerDTO container) throws IOException {
        List<DailyRecordEntry> dailyRecordEntryList = new ArrayList<>();
        for (DailyRecordEntryDTO dreDTO : container.getEntries()) {
            DailyRecordEntry dailyRecordEntry = new DailyRecordEntry();
            BeanUtils.copyProperties(dreDTO, dailyRecordEntry);
            dailyRecordEntry.setFood(serviceUtils.getFoodFromJSON(dreDTO.getFoodDTOJSON()));
            if (container.getRecordId() == null)
                dailyRecordEntry.setDailyRecord(record);
            else
                dailyRecordEntry.setDailyRecord(entityManager.getReference(DailyRecord.class, container.getRecordId()));
            dailyRecordEntryList.add(dailyRecordEntry);
        }
        return dailyRecordEntryList;
    }
}
