package net.shvdy.nutrition_tracker.service;

import net.shvdy.nutrition_tracker.dto.DailyRecordDTO;
import net.shvdy.nutrition_tracker.dto.NewEntriesContainerDTO;
import net.shvdy.nutrition_tracker.dto.UserProfileDTO;
import net.shvdy.nutrition_tracker.entity.DailyRecord;
import net.shvdy.nutrition_tracker.entity.UserProfile;
import net.shvdy.nutrition_tracker.repository.DailyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 19.03.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Service
public class DailyRecordService {
    private final DailyRecordRepository dailyRecordRepository;
    private Mapper mapper;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DailyRecordService(DailyRecordRepository dailyRecordRepository, Mapper mapper) {
        this.dailyRecordRepository = dailyRecordRepository;
        this.mapper = mapper;
    }

    public void saveNewEntries(NewEntriesContainerDTO newEntriesDTO) {
        mapper.setEntityManager(entityManager);
        dailyRecordRepository.save(mapper.getModelMapper().map(newEntriesDTO, DailyRecord.class));
    }

    public List<DailyRecordDTO> getWeeklyRecords(UserProfile userProfile, String requestedDay, Pageable pageable) {
        return insertBlankForAbsentDays(userProfile, requestedDay, pageable.getPageSize(),
                dailyRecordRepository.findByUserProfileAndRecordDateBetweenOrderByRecordDateDesc(
                        userProfile,
                        LocalDate.parse(requestedDay).minusDays(pageable.getPageSize() - 1).toString(),
                        requestedDay,
                        pageable)
                        .stream()
                        .map(x -> mapper.getModelMapper().map(x, DailyRecordDTO.class))
                        .collect(Collectors.toMap(DailyRecordDTO::getRecordDate, x -> x)));
    }

    private List<DailyRecordDTO> insertBlankForAbsentDays
            (UserProfile userProfile, String periodEndDate, int size, Map<String, DailyRecordDTO> weeklyRecords) {

        IntStream.range(0, size)
                .mapToObj(n -> LocalDate.parse(periodEndDate).minusDays(n).toString())
                .forEach(date -> weeklyRecords
                        .putIfAbsent(date, DailyRecordDTO.builder()
                                .recordDate(date)
                                .userProfile(mapper.getModelMapper().map(userProfile, UserProfileDTO.class))
                                .entries(new ArrayList<>()).build()));

        return new ArrayList<>(weeklyRecords.values()).stream()
                .sorted(Comparator.comparing(DailyRecordDTO::getRecordDate).reversed())
                .collect(Collectors.toList());
    }
}
