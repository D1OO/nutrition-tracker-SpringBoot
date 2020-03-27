package net.shvdy.sbproject.service;

import net.shvdy.sbproject.dto.DailyRecordDTO;
import net.shvdy.sbproject.dto.DailyRecordEntryDTO;
import net.shvdy.sbproject.dto.NewEntriesModalWindowDTO;
import net.shvdy.sbproject.entity.DailyRecord;
import net.shvdy.sbproject.entity.DailyRecordEntry;
import net.shvdy.sbproject.entity.Food;
import net.shvdy.sbproject.entity.UserProfile;
import net.shvdy.sbproject.repository.DailyRecordRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

/**
 * 19.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Service
public class DailyRecordService {

    private final DailyRecordRepository dailyRecordRepository;
    private ModelMapper modelMapper;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DailyRecordService(DailyRecordRepository dailyRecordRepository, ModelMapper modelMapper) {
        this.dailyRecordRepository = dailyRecordRepository;
        this.modelMapper = modelMapper;
    }

    public ArrayList<DailyRecordDTO> getPaginatedForUserAndLastDate(UserProfile userProfile, String lastDayOfPageRequest,
                                                                    Pageable pageable) {
        Page<DailyRecord> existingDailyRecords = dailyRecordRepository
                .queryByUserProfileAndRecordDateLessThanEqualOrderByRecordDateDesc
                        (userProfile, lastDayOfPageRequest, pageable);


        ArrayList<DailyRecord> listOfRecordsForAllDaysBeforeTheRequested = new ArrayList<>();
        ArrayList<DailyRecord> existing = existingDailyRecords.stream()
                .collect(toCollection(ArrayList::new));
        // Resulting set must be filled with records for those days, when the user didn't use the service
        // for example: to display not 26 Mar, 23 Mar, 17 Mar, but 26, 25, 24 with 25&24 inserted here
        if (existingDailyRecords.getNumber() < pageable.getPageSize()) {
            List<LocalDate> existingRecordsDates = existing
                    .stream().map(x -> LocalDate.parse(x.getRecordDate())).collect(toList());

            LocalDate firstDay = LocalDate.parse(lastDayOfPageRequest).minusDays(pageable.getPageSize());
            for (LocalDate day = LocalDate.parse(lastDayOfPageRequest); day.isAfter(firstDay); day = day.minusDays(1)) {
                String dayString = day.toString();
                if (!existingRecordsDates.contains(day))
                    listOfRecordsForAllDaysBeforeTheRequested.add(DailyRecord.builder().recordDate(dayString).build());
                else listOfRecordsForAllDaysBeforeTheRequested.add(existing.stream()
                        .filter(dr -> dr.getRecordDate().equals(dayString))
                        .findFirst().orElse(new DailyRecord()));
            }
            listOfRecordsForAllDaysBeforeTheRequested.sort(Comparator.comparing(DailyRecord::getRecordDate).reversed());
        }
        return modelMapper.map(listOfRecordsForAllDaysBeforeTheRequested, new TypeToken<ArrayList<DailyRecordDTO>>() {
        }.getType());
    }

    @Transactional
    public void saveNewEntries(NewEntriesModalWindowDTO newEntries) {
        DailyRecord newDailyRecord;
        newDailyRecord = DailyRecord.builder()
                .recordId(newEntries.getRecordId())
                .userProfile(entityManager.getReference(UserProfile.class, newEntries.getUserId()))
                .recordDate(newEntries.getRecordDate())
                .build();
        newDailyRecord.setEntries(DTOToEntityMapper(newDailyRecord, newEntries));
        entityManager.merge(newDailyRecord);
    }

    private List<DailyRecordEntry> DTOToEntityMapper(DailyRecord record, NewEntriesModalWindowDTO container) {
        List<DailyRecordEntry> dailyRecordEntryList = new ArrayList<>();
        for (DailyRecordEntryDTO dreDTO : container.getEntries()) {
            DailyRecordEntry dailyRecordEntry = new DailyRecordEntry();
            BeanUtils.copyProperties(dreDTO, dailyRecordEntry);
            dailyRecordEntry.setFood(entityManager.getReference(Food.class, dreDTO.getFoodId()));
            if (container.getRecordId() == null)
                dailyRecordEntry.setDailyRecord(record);
            else
                dailyRecordEntry.setDailyRecord(entityManager.getReference(DailyRecord.class, container.getRecordId()));
            dailyRecordEntryList.add(dailyRecordEntry);
        }
        return dailyRecordEntryList;
    }
}
