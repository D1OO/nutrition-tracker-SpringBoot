package net.shvdy.nutrition_tracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shvdy.nutrition_tracker.dto.DailyRecordEntryDTO;
import net.shvdy.nutrition_tracker.dto.FoodDTO;
import net.shvdy.nutrition_tracker.dto.NewEntriesContainerDTO;
import net.shvdy.nutrition_tracker.entity.DailyRecord;
import net.shvdy.nutrition_tracker.entity.DailyRecordEntry;
import net.shvdy.nutrition_tracker.entity.Food;
import net.shvdy.nutrition_tracker.entity.UserProfile;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 05.06.2020
 *
 * @author Dmitriy Storozhenko
 * @version 1.0
 */
@Component
@Scope("prototype")
public class Mapper {

    private EntityManager entityManager;
    private ModelMapper modelMapper = new ModelMapper();
    private ObjectMapper jacksonMapper = new ObjectMapper();

    public Mapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.createTypeMap(DailyRecordEntryDTO.class, DailyRecordEntry.class).setPostConverter(dailyRecordEntryConfig);
        modelMapper.createTypeMap(NewEntriesContainerDTO.class, DailyRecord.class).setPostConverter(newEntriesConfig);
    }

    Converter<DailyRecordEntryDTO, DailyRecordEntry> dailyRecordEntryConfig = context -> {
        try {
            context.getDestination().setFood(modelMapper.map(jacksonMapper.readValue(context.getSource().getFoodDTOJSON(), FoodDTO.class), Food.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return context.getDestination();
    };

    Converter<NewEntriesContainerDTO, DailyRecord> newEntriesConfig = context -> {
        context.getDestination().setUserProfile((entityManager.getReference(UserProfile.class, context.getSource().getProfileId())));

        List<DailyRecordEntry> entries = context.getSource().getEntries().stream()
                .map(x -> modelMapper.map(x, DailyRecordEntry.class)).collect(Collectors.toList());

        entries.forEach(x -> x.setDailyRecord(Optional.ofNullable(context.getSource().getRecordId()).isPresent() ?
                entityManager.getReference(DailyRecord.class, context.getSource().getRecordId()) : context.getDestination()));

        context.getDestination().setEntries(entries);
        return context.getDestination();
    };

    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    public ObjectMapper getJacksonMapper() {
        return jacksonMapper;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
