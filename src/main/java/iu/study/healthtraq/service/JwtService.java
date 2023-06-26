package iu.study.healthtraq.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import iu.study.healthtraq.exceptions.AuthenticationException;
import iu.study.healthtraq.properties.SecurityProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserService userDetailsService;
    private final SecurityProperties securityProperties;

    Logger logger = LoggerFactory.getLogger(JwtService.class);

    public String createToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        /*claims.put("auth", roles.stream()
                .map(s -> new SimpleGrantedAuthority(s.getAuthority()))
                .filter(Objects::nonNull).collect(Collectors.toList()));*/
        claims.put("roles", Collections.singletonList("admin"));

        Date now = new Date();
        long validityInMilliseconds = securityProperties.getAuthTokenValidityDurationHours() * 60 * 60 * 1000;
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, securityProperties.getSecurityKey())
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(securityProperties.getSecurityKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String resolveToken(@NotNull HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(securityProperties.getSecurityKey()).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Error parsing JWT", e);
            throw new AuthenticationException("Expired or invalid JWT token");
        }
    }
}
