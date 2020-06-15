package net.shvdy.nutrition_tracker.model.service;

import net.shvdy.nutrition_tracker.config.FormulaConfigProperties;
import net.shvdy.nutrition_tracker.dto.DailyRecordDTO;
import net.shvdy.nutrition_tracker.dto.NewEntriesContainerDTO;
import net.shvdy.nutrition_tracker.model.entity.DailyRecord;
import net.shvdy.nutrition_tracker.model.entity.DailyRecordEntry;
import net.shvdy.nutrition_tracker.model.entity.UserProfile;
import net.shvdy.nutrition_tracker.model.repository.DailyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
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
    private FormulaConfigProperties formulaConfigProperties;

    @Autowired
    public DailyRecordService(DailyRecordRepository dailyRecordRepository, FormulaConfigProperties formulaConfigProperties) {
        this.dailyRecordRepository = dailyRecordRepository;
        this.formulaConfigProperties = formulaConfigProperties;
    }

    public DailyRecord saveNewEntries(NewEntriesContainerDTO newEntriesDTO, UserProfile profile) {
        DailyRecord dailyRecord = findByDateAndProfile(newEntriesDTO.getRecordDate(), profile)
                .orElseGet(() -> DailyRecord.builder().entries(new ArrayList<>())
                        .recordDate(newEntriesDTO.getRecordDate()).userProfile(profile)
                        .dailyCaloriesNorm(getDailyCaloriesNorm(profile)).build());
        List<DailyRecordEntry> newEntries = newEntriesDTO.getEntries()
                .stream().map(e -> Mapper.MODEL.map(e, DailyRecordEntry.class)).collect(Collectors.toList());
        newEntries.forEach(e -> e.setDailyRecord(dailyRecord));
        dailyRecord.getEntries().addAll(newEntries);
        return dailyRecordRepository.save(dailyRecord);
    }

    public Optional<DailyRecord> findByDateAndProfile(String date, UserProfile profile) {
        return dailyRecordRepository.findByRecordDateAndUserProfile(date, profile);
    }

    public TreeSet<DailyRecordDTO> findByDatePeriod(UserProfile userProfile, String periodEndDate, int size) {
        return insertBlankForAbsentDays(periodEndDate, size,
                dailyRecordRepository
                        .findByUserProfileAndRecordDateBetween(userProfile,
                                LocalDate.parse(periodEndDate).minusDays(size - 1).toString(), periodEndDate)
                        .stream().map(x -> Mapper.MODEL.map(x, DailyRecordDTO.class))
                        .collect(Collectors.toMap(DailyRecordDTO::getRecordDate, x -> x)));
    }

    private TreeSet<DailyRecordDTO> insertBlankForAbsentDays
            (String periodEndDate, int size, Map<String, DailyRecordDTO> weeklyRecords) {

        IntStream.range(0, size).mapToObj(n -> LocalDate.parse(periodEndDate).minusDays(n).toString())
                .forEach(date -> weeklyRecords.putIfAbsent(date, DailyRecordDTO.builder().recordDate(date).build()));
        return new TreeSet<>(weeklyRecords.values());
    }

    public int getDailyCaloriesNorm(UserProfile userProfile) {
        return (int) (formulaConfigProperties.coef1
                + formulaConfigProperties.weightModifier * userProfile.getWeight()
                + formulaConfigProperties.heightModifier * userProfile.getHeight()
                - formulaConfigProperties.ageModifier * userProfile.getAge()
                * userProfile.getLifestyle().getFactor());
    }
}
