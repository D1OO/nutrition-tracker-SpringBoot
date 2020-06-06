package net.shvdy.nutrition_tracker.service;

import net.shvdy.nutrition_tracker.config.FormulaConfigProperties;
import net.shvdy.nutrition_tracker.dto.DailyRecordDTO;
import net.shvdy.nutrition_tracker.dto.NewEntriesContainerDTO;
import net.shvdy.nutrition_tracker.dto.UserProfileDTO;
import net.shvdy.nutrition_tracker.entity.DailyRecord;
import net.shvdy.nutrition_tracker.entity.UserProfile;
import net.shvdy.nutrition_tracker.repository.DailyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    private FormulaConfigProperties formulaConfigProperties;

    @Autowired
    public DailyRecordService(DailyRecordRepository dailyRecordRepository, FormulaConfigProperties formulaConfigProperties) {
        this.dailyRecordRepository = dailyRecordRepository;
        this.formulaConfigProperties = formulaConfigProperties;
    }

    public void saveNewEntries(NewEntriesContainerDTO newEntriesDTO) {
        dailyRecordRepository.save(Mapper.MODEL.map(newEntriesDTO, DailyRecord.class));
    }

    public List<DailyRecordDTO> getWeeklyRecords(UserProfile userProfile, String periodEndDate, Pageable pageable) {
        return insertBlankForAbsentDays(userProfile, periodEndDate, pageable.getPageSize(),
                dailyRecordRepository.findByUserProfileAndRecordDateBetween(
                        userProfile,
                        LocalDate.parse(periodEndDate).minusDays(pageable.getPageSize() - 1).toString(),
                        periodEndDate,
                        pageable).stream()
                        .map(x -> Mapper.MODEL.map(x, DailyRecordDTO.class))
                        .collect(Collectors.toMap(DailyRecordDTO::getRecordDate, x -> x)));
    }

    private List<DailyRecordDTO> insertBlankForAbsentDays
            (UserProfile userProfile, String periodEndDate, int size, Map<String, DailyRecordDTO> weeklyRecords) {

        IntStream.range(0, size)
                .mapToObj(n -> LocalDate.parse(periodEndDate).minusDays(n).toString())
                .forEach(date -> weeklyRecords
                        .putIfAbsent(date, DailyRecordDTO.builder()
                                .recordDate(date)
                                .userProfile(Mapper.MODEL.map(userProfile, UserProfileDTO.class))
                                .dailyCaloriesNorm(getDailyCaloriesNorm(userProfile))
                                .entries(new ArrayList<>()).build()));

        return new ArrayList<>(weeklyRecords.values()).stream()
                .sorted(Comparator.comparing(DailyRecordDTO::getRecordDate).reversed())
                .collect(Collectors.toList());
    }

    public int getDailyCaloriesNorm(UserProfile userProfile) {
        return (int)( formulaConfigProperties.coef1
                + formulaConfigProperties.weightModifier * userProfile.getWeight()
                + formulaConfigProperties.heightModifier * userProfile.getHeight()
                - formulaConfigProperties.ageModifier * userProfile.getAge()
                * userProfile.getLifestyle().getFactor());
    }
}
