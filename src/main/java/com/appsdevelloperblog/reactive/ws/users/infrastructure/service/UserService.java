package com.appsdevelloperblog.reactive.ws.users.infrastructure.service;

import com.appsdevelloperblog.reactive.ws.users.infrastructure.data.UserEntity;
import com.appsdevelloperblog.reactive.ws.users.infrastructure.presentation.CreateUserRequest;
import com.appsdevelloperblog.reactive.ws.users.infrastructure.presentation.UserRest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {

    Mono<UserRest> createUser(Mono<CreateUserRequest> createUserRequestMono);

    Mono<UserRest> getUserById(UUID id);

    Flux<UserRest> findAll(int page, int limit);
}
