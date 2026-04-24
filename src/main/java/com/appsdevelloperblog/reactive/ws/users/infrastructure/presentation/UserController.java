package com.appsdevelloperblog.reactive.ws.users.infrastructure.presentation;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<UserRest>> createUser(@RequestBody @Valid Mono<CreateUserRequest> createUserRequest){
        return createUserRequest.map(
                request ->
                        new UserRest(
                                UUID.randomUUID(),
                                request.getFirstName(),
                                request.getLastName(),
                                request.getEmail())
                                    ).map(
                        userRest -> ResponseEntity
                                .status(HttpStatus.CREATED)
                                .location(URI.create("/users/" + userRest.getId()))
                                .body(userRest)
                                            );

    }

    @GetMapping("/{userId}")
    public Mono<UserRest> getUser(@PathVariable("userId") UUID userId){
        return Mono.just(
                new UserRest(
                        userId,
                        "Sergey",
                        "Kargopolov",
                        "test@test.com"
                )
        );
    }
}
