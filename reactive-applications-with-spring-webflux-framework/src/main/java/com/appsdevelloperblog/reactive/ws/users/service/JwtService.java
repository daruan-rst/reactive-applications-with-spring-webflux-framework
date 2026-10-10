package com.appsdevelloperblog.reactive.ws.users.service;

import reactor.core.publisher.Mono;

public interface JwtService {

    String generateJwt(String subject);

    Mono<Boolean> validateToken(String token);

    String extractTokenSubject(String token);
}
