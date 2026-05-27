package com.appsdevelloperblog.reactive.ws.users.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtServiceImpl implements JwtService{

    private final Environment environment;

    public JwtServiceImpl(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String generateJwt(String subject) {
        return Jwts
                .builder()
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey(){
        return Optional.ofNullable(environment.getProperty("token.secret"))
                .map(tokenSecret -> tokenSecret.getBytes())
                .map(tokenSecretBytes -> Keys.hmacShaKeyFor(tokenSecretBytes))
                .orElseThrow(() -> new IllegalArgumentException("token.secret must be configured in the properties"));
    }
}
