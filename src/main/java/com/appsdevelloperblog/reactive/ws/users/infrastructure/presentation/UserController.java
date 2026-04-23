package com.appsdevelloperblog.reactive.ws.users.infrastructure.presentation;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public Mono<UserRest> createUser(@RequestBody @Valid Mono<CreateUserRequest> createUserRequest){
        return createUserRequest.map(
                request ->
                        new UserRest(
                                UUID.randomUUID(),
                                request.getFirstName(),
                                request.getLastName(),
                                request.getEmail())
                                    );

    }
}
