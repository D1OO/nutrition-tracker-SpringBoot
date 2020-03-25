package net.shvdy.sbproject.service;

import net.shvdy.sbproject.dto.DailyRecordDTO;
import net.shvdy.sbproject.dto.DailyRecordEntryDTO;
import net.shvdy.sbproject.dto.NewEntriesModalWindowDTO;
import net.shvdy.sbproject.entity.DailyRecord;
import net.shvdy.sbproject.entity.DailyRecordEntry;
import net.shvdy.sbproject.entity.Food;
import net.shvdy.sbproject.repository.DailyRecordRepository;
import net.shvdy.sbproject.service.exception.RecordDoesntExistException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private EntityManager entityManager;

    @Autowired
    public DailyRecordService(DailyRecordRepository dailyRecordRepository, ModelMapper modelMapper, EntityManager entityManager) {
        this.dailyRecordRepository = dailyRecordRepository;
        this.modelMapper = modelMapper;
        this.entityManager = entityManager;
    }

    public DailyRecordDTO getForUserAndDate(Long userId, String date) {
        Optional<DailyRecord> dailyRecord = dailyRecordRepository.findByUserIdAndRecordDate(userId, date);
        if (dailyRecord.isPresent())
            return modelMapper.map(dailyRecord.get(), DailyRecordDTO.class);
        else {
            DailyRecord newRecord = dailyRecordRepository.save(DailyRecord.builder()
                    .userId(userId).recordDate(date).build());
            return modelMapper.map(newRecord, DailyRecordDTO.class);
        }
    }

    public void saveNewEntriesToRecordWithId(Long recordId, NewEntriesModalWindowDTO newEntries) throws RecordDoesntExistException {
        Optional<DailyRecord> dailyRecord = dailyRecordRepository.findById(recordId);
        if (dailyRecord.isPresent()) {
            DailyRecord dr = dailyRecord.get();
            List<DailyRecordEntry> e = DTOToEntityMapper(newEntries);
            dr.getEntries().addAll(e);
            dailyRecordRepository.save(dr);
        } else
            throw new RecordDoesntExistException();
    }

    private List<DailyRecordEntry> DTOToEntityMapper(NewEntriesModalWindowDTO container) {
        List<DailyRecordEntry> dailyRecordEntryList = new ArrayList<>();
        for (DailyRecordEntryDTO dreDTO : container.getEntries()) {
            DailyRecordEntry dre = new DailyRecordEntry();
            BeanUtils.copyProperties(dreDTO, dre);
            dre.setFood(entityManager.getReference(Food.class, dreDTO.getFoodId()));
            dre.setDailyRecord(entityManager.getReference(DailyRecord.class, container.getRecordId()));
            dailyRecordEntryList.add(dre);
        }
        return dailyRecordEntryList;
    }
}
