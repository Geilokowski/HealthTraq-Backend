package iu.study.healthtraq.service;

import iu.study.api.polar.model.ExerciseHashId;
import iu.study.api.polar.model.HeartRate;
import iu.study.healthtraq.models.HealthtraqExercise;
import iu.study.healthtraq.models.Participant;
import iu.study.healthtraq.repositories.ExerciseRepository;
import iu.study.healthtraq.repositories.ParticipantRepository;
import iu.study.healthtraq.utils.PolarActivity;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final PolarApiService polarApiService;
    private final ExerciseRepository exerciseRepository;
    private final ParticipantRepository participantRepository;

    Logger logger = LoggerFactory.getLogger(JwtService.class);

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

    @Async
    public void refreshForParticipant(Participant participant){
        try {
            polarApiService
                    .getAllExercises(participant)
                    .forEach((exercise) -> createLocalCopyOfPolarExercise(participant, exercise));
        }catch(Exception ex){
            logger.error("Failed to fetch exercises for participant " + participant.getId(), ex);
        }

        participant.setFetchingData(false);
        participantRepository.save(participant);
    }

    public void createLocalCopyOfPolarExercise(
            @NotNull Participant participant,
            @NotNull ExerciseHashId polarExercise) {
        if(exerciseRepository.existsBySourceId(polarExercise.getId()))
            return;

        final var heartRateNullable = Optional.ofNullable(polarExercise.getHeartRate());
        HealthtraqExercise healthtraqExercise = HealthtraqExercise.builder()
                .sourceId(polarExercise.getId())
                .participant(participant)
                .averageHeartRate(heartRateNullable.map(HeartRate::getAverage).orElse(null))
                .maximumHeartRate(heartRateNullable.map(HeartRate::getMaximum).orElse(null))
                .calories(polarExercise.getCalories())
                .distance(polarExercise.getDistance())
                .startTime(getPolarZonedTime(polarExercise.getStartTime(), polarExercise.getStartTimeUtcOffset()))
                .duration(Optional.ofNullable(polarExercise.getDuration()).map(Duration::parse).orElse(null))
                .activity(PolarActivity.fromString(polarExercise.getDetailedSportInfo()))
                .build();
        exerciseRepository.save(healthtraqExercise);
    }
}
