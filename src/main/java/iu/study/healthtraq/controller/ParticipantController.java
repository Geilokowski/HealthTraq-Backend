package iu.study.healthtraq.controller;

import iu.study.healthtraq.models.Participant;
import iu.study.healthtraq.repositories.ExerciseRepository;
import iu.study.healthtraq.repositories.ParticipantRepository;
import iu.study.healthtraq.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParticipantController {
    private final ExerciseService exerciseService;
    private final ExerciseRepository exerciseRepository;
    private final ParticipantRepository participantRepository;

    private record ParticipantDTO(
            long id,
            String firstName,
            String lastName,
            boolean fetchingData,
            long exerciseCount,
            String dataProvider) {
        public ParticipantDTO(@NotNull Participant participant){
            this(participant.getId(), participant.getFirstName(), participant.getLastName(),
                    participant.isFetchingData(),
                    participant.getExercises().size(), "POLAR");
        }
    }

    private record ParticipantDTOList(
            List<ParticipantDTO> participants) { }

    @GetMapping(path = "/participants")
    public ResponseEntity<ParticipantDTOList> getParticipants() {
        final var participants = participantRepository
                .findAll().stream()
                .map(ParticipantDTO::new)
                .toList();
        return ResponseEntity.ok(new ParticipantDTOList(participants));
    }

    @PostMapping(path = "/participants/{participantId}/exercises")
    public ResponseEntity<?> refreshExercises(@PathVariable long participantId) {
        Participant participant = participantRepository
                .findById(participantId)
                .orElseThrow();
        participant.setFetchingData(true);
        participantRepository.save(participant);
        exerciseService.refreshForParticipant(participant);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping(path = "/participants/{participantId}")
    public ResponseEntity<?> deleteParticipant(@PathVariable long participantId) {
        exerciseRepository.deleteAllByParticipantId(participantId);
        participantRepository.deleteById(participantId);
        return ResponseEntity.ok().build();
    }
}
