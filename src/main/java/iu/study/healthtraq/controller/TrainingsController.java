package iu.study.healthtraq.controller;

import iu.study.healthtraq.models.HealthtraqExercise;
import iu.study.healthtraq.repositories.ExerciseRepository;
import iu.study.healthtraq.utils.PolarActivity;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrainingsController {
    private final ExerciseRepository exerciseRepository;

    private record TrainingsDTOList(List<TrainingsDTO> trainings) { }

    private record TrainingsDTO(
            long id,
            ZonedDateTime startTime,
            ZonedDateTime endTime,
            Float distance,
            Integer calories,
            String sourceId,
            Integer averageHeartRate,
            Integer maximumHeartRate,
            PolarActivity activity) {
        public TrainingsDTO(@NotNull HealthtraqExercise databaseEntry) {
            this(databaseEntry.getId(),
                    databaseEntry.getStartTime(),
                    databaseEntry.getEndTime(),
                    databaseEntry.getDistance(),
                    databaseEntry.getCalories(),
                    databaseEntry.getSourceId(),
                    databaseEntry.getAverageHeartRate(),
                    databaseEntry.getMaximumHeartRate(),
                    databaseEntry.getActivity());
        }
    }

    @GetMapping(path = "/excercises")
    public ResponseEntity<TrainingsDTOList> test() {
        List<TrainingsDTO> trainingsDTOs = exerciseRepository
                .findAll().stream()
                .map(TrainingsDTO::new)
                .toList();
        return ResponseEntity.ok(new TrainingsDTOList(trainingsDTOs));
    }
}
