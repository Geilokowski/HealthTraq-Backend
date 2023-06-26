package iu.study.healthtraq.config;

import iu.study.healthtraq.exceptions.AuthenticationException;
import iu.study.healthtraq.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest httpServletRequest,
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = jwtService.resolveToken(httpServletRequest);
        try {
            if (token != null && jwtService.validateToken(token)) {
                Authentication auth = jwtService.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
