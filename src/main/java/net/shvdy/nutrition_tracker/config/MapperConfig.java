package net.shvdy.nutrition_tracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shvdy.nutrition_tracker.dto.DailyRecordDTO;
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

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class MapperConfig {

    protected EntityManager entityManager = null;
    protected ModelMapper modelMapper = new ModelMapper();
    protected ObjectMapper jacksonMapper = new ObjectMapper();

    public MapperConfig() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.createTypeMap(DailyRecordEntryDTO.class, DailyRecordEntry.class).setPostConverter(dailyRecordEntryConfig);
        modelMapper.createTypeMap(NewEntriesContainerDTO.class, DailyRecord.class).setPostConverter(newEntriesConfig);
        modelMapper.createTypeMap(DailyRecord.class, DailyRecordDTO.class).setPostConverter(dailyRecordConfig);
    }

    protected Converter<DailyRecordEntryDTO, DailyRecordEntry> dailyRecordEntryConfig = context -> {
        try {
            context.getDestination().setFood(modelMapper
                    .map(jacksonMapper.readValue(context.getSource().getFoodDTOJSON(), FoodDTO.class), Food.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return context.getDestination();
    };

    protected Converter<NewEntriesContainerDTO, DailyRecord> newEntriesConfig = context -> {
        context.getDestination()
                .setUserProfile((entityManager.getReference(UserProfile.class, context.getSource().getProfileId())));

        List<DailyRecordEntry> entries = context.getSource().getEntries().stream()
                .map(x -> modelMapper.map(x, DailyRecordEntry.class)).collect(Collectors.toList());

        entries.forEach(x -> x.setDailyRecord(Optional.ofNullable(context.getSource().getRecordId()).isPresent() ?
                entityManager.getReference(DailyRecord.class, context.getSource().getRecordId()) : context.getDestination()));

        context.getDestination().setEntries(entries);
        return context.getDestination();
    };

    protected Converter<DailyRecord, DailyRecordDTO> dailyRecordConfig = context -> {
        context.getDestination()
                .setTotalCalories(context.getDestination().getEntries()
                        .stream().mapToInt(x -> x.getFood().getCalories() * x.getQuantity() / 100).sum());

        context.getDestination()
                .setPercentage((int) (context.getDestination().getTotalCalories() /
                        (double) context.getDestination().getDailyCaloriesNorm() * 100));

        context.getDestination()
                .setTotalFats(context.getDestination().getEntries().stream()
                        .mapToInt(x -> x.getFood().getFats() * x.getQuantity() / 100).sum());

        context.getDestination()
                .setTotalProteins(context.getDestination().getEntries().stream()
                        .mapToInt(x -> x.getFood().getProteins() * x.getQuantity() / 100).sum());

        context.getDestination()
                .setTotalCarbs(context.getDestination().getEntries().stream()
                        .mapToInt(x -> x.getFood().getCarbohydrates() * x.getQuantity() / 100).sum());

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
