package com.appsdevelloperblog.reactive.ws.users.infrastructure.service;

import com.appsdevelloperblog.reactive.ws.users.infrastructure.presentation.CreateUserRequest;
import com.appsdevelloperblog.reactive.ws.users.infrastructure.presentation.UserRest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public Mono<UserRest> createUser(Mono<CreateUserRequest> createUserRequestMono) {
        return null;
    }
}
