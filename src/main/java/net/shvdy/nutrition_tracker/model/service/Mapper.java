package net.shvdy.nutrition_tracker.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import net.shvdy.nutrition_tracker.dto.DailyRecordDTO;
import net.shvdy.nutrition_tracker.dto.DailyRecordEntryDTO;
import net.shvdy.nutrition_tracker.dto.FoodDTO;
import net.shvdy.nutrition_tracker.dto.NewEntriesContainerDTO;
import net.shvdy.nutrition_tracker.model.entity.DailyRecord;
import net.shvdy.nutrition_tracker.model.entity.DailyRecordEntry;
import net.shvdy.nutrition_tracker.model.entity.Food;
import net.shvdy.nutrition_tracker.model.entity.UserProfile;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.Optional;

@Log4j2
@Component
public class Mapper {

    public static final ModelMapper MODEL = new ModelMapper();
    public static final ObjectMapper JACKSON = new ObjectMapper();
    @PersistenceContext
    private EntityManager entityManager;

    private static Converter<DailyRecordEntryDTO, DailyRecordEntry> dailyRecordEntryConfig = context -> {
        try {
            context.getDestination().setFood(MODEL
                    .map(JACKSON.readValue(context.getSource().getFoodDTOJSON(), FoodDTO.class), Food.class));
        } catch (IOException e) {
            log.error(e);
        }
        return context.getDestination();
    };

    private Converter<NewEntriesContainerDTO, DailyRecord> newEntriesConfig = context -> {
        if (Optional.ofNullable(context.getSource().getRecordId()).isPresent()) {
            DailyRecord d = entityManager.getReference(DailyRecord.class, context.getSource().getRecordId());
            context.getDestination().getEntries().forEach(x -> x.setDailyRecord(d));
        } else {
            context.getDestination().setUserProfile(entityManager
                    .getReference(UserProfile.class, context.getSource().getProfileId()));
        }
        return context.getDestination();
    };

    private static Converter<DailyRecord, DailyRecordDTO> dailyRecordConfig = context -> {
        context.getDestination()
                .setTotalCalories(context.getDestination().getEntries()
                        .stream().mapToInt(e -> e.getFood().getCalories() * e.getQuantity() / 100).sum());

        context.getDestination()
                .setPercentage((int) (context.getDestination().getTotalCalories() /
                        (double) context.getDestination().getDailyCaloriesNorm() * 100));

        context.getDestination()
                .setTotalFats(context.getDestination().getEntries().stream()
                        .mapToInt(e -> e.getFood().getFats() * e.getQuantity() / 100).sum());

        context.getDestination()
                .setTotalProteins(context.getDestination().getEntries().stream()
                        .mapToInt(e -> e.getFood().getProteins() * e.getQuantity() / 100).sum());

        context.getDestination()
                .setTotalCarbs(context.getDestination().getEntries().stream()
                        .mapToInt(x -> x.getFood().getCarbohydrates() * x.getQuantity() / 100).sum());

        return context.getDestination();
    };

    static {
        MODEL.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        MODEL.createTypeMap(DailyRecordEntryDTO.class, DailyRecordEntry.class).setPostConverter(dailyRecordEntryConfig);
        MODEL.createTypeMap(DailyRecord.class, DailyRecordDTO.class).setPostConverter(dailyRecordConfig);
    }

    @PostConstruct
    public void init() {
        MODEL.createTypeMap(NewEntriesContainerDTO.class, DailyRecord.class).setPostConverter(newEntriesConfig);
    }

}
