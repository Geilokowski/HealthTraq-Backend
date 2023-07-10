package iu.study.healthtraq.service;

import iu.study.api.polar.model.ExerciseHashId;
import iu.study.healthtraq.models.HealthtraqExercise;
import iu.study.healthtraq.repositories.ExerciseRepository;
import iu.study.healthtraq.utils.PolarActivity;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    /**
     * I dont know if this is correct, someone please check if the timezones are actually how they
     * are supposed to be, maybe validate with the table on the polar flow website?
     * @param str The string without the timezone in ISO format
     * @param utcOffset The UTC offset in minutes
     * @return A ZonedDateTime object with the correct timezone (at least it should)
     */
    @Contract(pure = true)
    private @Nullable ZonedDateTime getPolarZonedTime(String str, Integer utcOffset){
        if(str == null || utcOffset == null)
            return null;

        // Will break for some timezones but who cares
        int offsetHours = utcOffset / 60;
        String timezoneSuffix = offsetHours < 0 ?
                String.format("%03d", offsetHours) :
                "+" + String.format("%02d", offsetHours);
        String finalString = str + timezoneSuffix + ":00";
        return ZonedDateTime.parse(finalString);
    }

    public void createLocalCopyOfPolarExercise(@NotNull ExerciseHashId polarExercise) {
        if(exerciseRepository.existsBySourceId(polarExercise.getId()))
            return;

        HealthtraqExercise healthtraqExercise = HealthtraqExercise.builder()
                .sourceId(polarExercise.getId())
                .startTime(getPolarZonedTime(polarExercise.getStartTime(), polarExercise.getStartTimeUtcOffset()))
                .activity(PolarActivity.fromString(polarExercise.getDetailedSportInfo()))
                .build();
        exerciseRepository.save(healthtraqExercise);
    }
}
