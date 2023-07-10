package iu.study.healthtraq.controller;

import iu.study.api.polar.ApiException;
import iu.study.healthtraq.repositories.ParticipantRepository;
import iu.study.healthtraq.service.ExerciseService;
import iu.study.healthtraq.service.PolarApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class AddParticipantController {
    private final ExerciseService exerciseService;
    private final ParticipantRepository participantRepository;
    private final PolarApiService polarApiService;

    @GetMapping(path = "/partners/polar/test")
    public ResponseEntity<String> test() {
        var participant = participantRepository
                .findAll().stream()
                .filter(savedParticipant -> savedParticipant.getLastName().equals("Leipzig"))
                .findFirst()
                .orElseThrow();
        polarApiService
                .getAllExercises(participant)
                .forEach(exerciseService::createLocalCopyOfPolarExercise);
        return ResponseEntity.ok("success");
    }

    @GetMapping(path = "/partners/polar/success")
    public ResponseEntity<String> participantReturnUri(@RequestParam String code) throws ApiException {
        polarApiService.registerParticipant(code);
        return ResponseEntity.ok(code);
    }

    @GetMapping(path = "/partners/polar/start")
    public RedirectView startAuthorizationFlow(){
        return new RedirectView(polarApiService.getAuthorizationUrl());
    }
}
