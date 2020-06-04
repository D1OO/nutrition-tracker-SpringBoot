package net.shvdy.nutrition_tracker.service;

import net.shvdy.nutrition_tracker.dto.DailyRecordDTO;
import net.shvdy.nutrition_tracker.dto.NewEntriesContainerDTO;
import net.shvdy.nutrition_tracker.entity.DailyRecord;
import net.shvdy.nutrition_tracker.entity.UserProfile;
import net.shvdy.nutrition_tracker.repository.DailyRecordRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private DailyRecordUtils dailyRecordUtils;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DailyRecordService(DailyRecordRepository dailyRecordRepository, ModelMapper modelMapper,
                              DailyRecordUtils dailyRecordUtils) {
        this.dailyRecordRepository = dailyRecordRepository;
        this.modelMapper = modelMapper;
        this.dailyRecordUtils = dailyRecordUtils;
    }

    public void saveNewEntries(NewEntriesContainerDTO newEntries) throws IOException {
        DailyRecord newDailyRecord;
        newDailyRecord = DailyRecord.builder()
                .recordId(newEntries.getRecordId())
                .userProfile(entityManager.getReference(UserProfile.class, newEntries.getProfileId()))
                .recordDate(newEntries.getRecordDate())
                .build();
        newDailyRecord.setEntries(dailyRecordUtils.mapDTOtoEntity(newDailyRecord, newEntries));
        dailyRecordRepository.save(newDailyRecord);
    }

    public ArrayList<DailyRecordDTO> getWeeklyRecords(UserProfile userProfile, String requestedDay, Pageable pageable) {
        //////////////
        requestedDay = (requestedDay == null) ?
                LocalDate.now().toString() : LocalDate.parse(requestedDay).minusDays(1).toString();

        Page<DailyRecord> existingDailyRecords = dailyRecordRepository
                .findByUserProfileAndRecordDateLessThanEqualOrderByRecordDateDesc(userProfile, requestedDay, pageable);

        ArrayList<DailyRecord> existingRecords = existingDailyRecords.stream().collect(toCollection(ArrayList::new));
        List<LocalDate> existingRecordsDates = existingRecords.stream().map(x -> LocalDate.parse(x.getRecordDate())).collect(toList());
        LocalDate dayWeekBeforeTheRequested = LocalDate.parse(requestedDay).minusDays(pageable.getPageSize());
        ArrayList<DailyRecord> recordsForEachDayBeforeTheRequested = new ArrayList<>();

        for (LocalDate day = LocalDate.parse(requestedDay); day.isAfter(dayWeekBeforeTheRequested); day = day.minusDays(1)) {
            String dayString = day.toString();
            if (existingRecordsDates.contains(day)) {
                recordsForEachDayBeforeTheRequested.add(existingRecords.stream()
                        .filter(dr -> dr.getRecordDate().equals(dayString))
                        .findFirst().orElse(new DailyRecord()));
            } else
                recordsForEachDayBeforeTheRequested.add(DailyRecord.builder().recordDate(dayString).build());
        }
        return modelMapper.map(recordsForEachDayBeforeTheRequested, new TypeToken<ArrayList<DailyRecordDTO>>() {
        }.getType());
    }
}
