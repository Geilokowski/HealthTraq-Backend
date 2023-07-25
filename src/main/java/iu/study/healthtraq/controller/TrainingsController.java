package iu.study.healthtraq.controller;

import iu.study.healthtraq.models.HealthtraqExercise;
import iu.study.healthtraq.repositories.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TrainingsController {
    private final ExerciseRepository exerciseRepository;

    private record TrainingsDTOList(List<TrainingsDTO> trainings) { }

    private record TrainingsDTO(
            long id,
            String participantName,
            ZonedDateTime startTime,
            Long trainingsSeconds,
            Float distance,
            Integer calories,
            String sourceId,
            Integer averageHeartRate,
            Integer maximumHeartRate,
            String activity) {
        public TrainingsDTO(@NotNull HealthtraqExercise databaseEntry) {
            this(databaseEntry.getId(),
                    databaseEntry.getParticipant() != null ?
                            databaseEntry.getParticipant().getFirstName() + " " +
                            databaseEntry.getParticipant().getLastName() : "",
                    databaseEntry.getStartTime(),
                    Optional.ofNullable(databaseEntry.getDuration())
                            .map(Duration::toSeconds)
                            .orElse(null),
                    databaseEntry.getDistance(),
                    databaseEntry.getCalories(),
                    databaseEntry.getSourceId(),
                    databaseEntry.getAverageHeartRate(),
                    databaseEntry.getMaximumHeartRate(),
                    databaseEntry.getActivity().getLabel());
        }
    }

    @GetMapping(path = "/exercises")
    public ResponseEntity<TrainingsDTOList> test() {
        List<TrainingsDTO> trainingsDTOs = exerciseRepository
                .findAll().stream()
                .map(TrainingsDTO::new)
                .toList();
        return ResponseEntity.ok(new TrainingsDTOList(trainingsDTOs));
    }
}
