package net.shvdy.sbproject.service;

import net.shvdy.sbproject.dto.DailyRecordDTO;
import net.shvdy.sbproject.dto.NewEntriesContainerDTO;
import net.shvdy.sbproject.entity.DailyRecord;
import net.shvdy.sbproject.entity.UserProfile;
import net.shvdy.sbproject.repository.DailyRecordRepository;
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

    public ArrayList<DailyRecordDTO> getPaginatedForUserAndLastDate(UserProfile userProfile, String lastDayOfPageRequest,
                                                                    Pageable pageable) {
        lastDayOfPageRequest = (lastDayOfPageRequest == null) ?
                LocalDate.now().toString() : LocalDate.parse(lastDayOfPageRequest).minusDays(1).toString();

        Page<DailyRecord> existingDailyRecords = dailyRecordRepository
                .queryByUserProfileAndRecordDateLessThanEqualOrderByRecordDateDesc(userProfile, lastDayOfPageRequest, pageable);

        ArrayList<DailyRecord> recordsForAllDaysBeforeTheRequested = new ArrayList<>();
        ArrayList<DailyRecord> existing = existingDailyRecords.stream()
                .collect(toCollection(ArrayList::new));
        List<LocalDate> existingRecordsDates = existing
                .stream().map(x -> LocalDate.parse(x.getRecordDate())).collect(toList());
        LocalDate firstDay = LocalDate.parse(lastDayOfPageRequest).minusDays(pageable.getPageSize());

        for (LocalDate day = LocalDate.parse(lastDayOfPageRequest); day.isAfter(firstDay); day = day.minusDays(1)) {
            String dayString = day.toString();
            if (!existingRecordsDates.contains(day))
                recordsForAllDaysBeforeTheRequested.add(DailyRecord.builder().recordDate(dayString).build());
            else {
                recordsForAllDaysBeforeTheRequested.add(existing.stream()
                        .filter(dr -> dr.getRecordDate().equals(dayString))
                        .findFirst().orElse(new DailyRecord()));
            }
        }
        return modelMapper.map(recordsForAllDaysBeforeTheRequested, new TypeToken<ArrayList<DailyRecordDTO>>() {
        }.getType());
    }
}
