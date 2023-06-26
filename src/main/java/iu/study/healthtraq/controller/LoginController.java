package iu.study.healthtraq.controller;

import iu.study.healthtraq.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final AuthService authService;

    private record LoginRequestBody(String username, String password) { }
    private record LoginResponseBody(String token) { }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseBody> login(@RequestBody @NotNull LoginRequestBody requestBody) {
        String token = authService.login(requestBody.username(), requestBody.password());
        return ResponseEntity.ok(new LoginResponseBody(token));
    }
}
