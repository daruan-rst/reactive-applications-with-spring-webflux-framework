package com.appsdevelloperblog.reactive.ws.users.infrastructure.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public void createUser(@RequestBody Mono<CreateUserRequest> createUserRequest){


    }
}
