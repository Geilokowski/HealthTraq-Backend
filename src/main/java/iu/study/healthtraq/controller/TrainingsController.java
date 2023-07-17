package iu.study.healthtraq.controller;

import iu.study.healthtraq.repositories.ExerciseRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrainingsController {
    private final ExerciseRepository exerciseRepository;

    private record TraningsDTOList(List<TraningsDTO> trainings) { }

    private record TraningsDTO(
            String date){ }

    @Data
    @AllArgsConstructor
    private static class TraningsDTOClass {
        private final String date;
    }

    @GetMapping(path = "/partners/polar/test")
    public ResponseEntity<TraningsDTOList> test() {
        TraningsDTO dto = new TrainingsController.TraningsDTO("10.10.1990");

        return ResponseEntity.ok(new TraningsDTOList(Collections.emptyList()));
    }
}
