package net.shvdy.nutrition_tracker.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import net.shvdy.nutrition_tracker.dto.DailyRecordDTO;
import net.shvdy.nutrition_tracker.dto.DailyRecordEntryDTO;
import net.shvdy.nutrition_tracker.dto.FoodDTO;
import net.shvdy.nutrition_tracker.model.entity.DailyRecord;
import net.shvdy.nutrition_tracker.model.entity.DailyRecordEntry;
import net.shvdy.nutrition_tracker.model.entity.Food;
import net.shvdy.nutrition_tracker.model.entity.UserProfile;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Log4j2
@Component
public class Mapper {

    public static final ModelMapper MODEL = new ModelMapper();
    public static final ObjectMapper JACKSON = new ObjectMapper();

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

    private static Converter<DailyRecordDTO, DailyRecord> dailyRecordConfig2 = context -> {
        context.getDestination().setUserProfile(UserProfile.builder()
                .profileId(context.getSource().getProfileId()).build());
        return context.getDestination();
    };

    static {
        MODEL.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        MODEL.createTypeMap(DailyRecord.class, DailyRecordDTO.class).setPostConverter(dailyRecordConfig);
        MODEL.createTypeMap(DailyRecordDTO.class, DailyRecord.class).setPostConverter(dailyRecordConfig2);
    }

    private Converter<DailyRecordEntryDTO, DailyRecordEntry> dailyRecordEntryConfig = context -> {
        try {
            context.getDestination().setFood(MODEL
                    .map(JACKSON.readValue(context.getSource().getFoodDTOJSON(), FoodDTO.class), Food.class));
        } catch (IOException e) {
            log.error(e);
        }
        return context.getDestination();
    };

    @PostConstruct
    private void init() {
        MODEL.createTypeMap(DailyRecordEntryDTO.class, DailyRecordEntry.class).setPostConverter(dailyRecordEntryConfig);
    }

}
