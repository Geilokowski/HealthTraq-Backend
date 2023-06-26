package iu.study.healthtraq.service;

import iu.study.healthtraq.exceptions.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtService.createToken(username);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Invalid username/password supplied");
        }
    }
}
