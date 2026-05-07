package com.appsdevelloperblog.reactive.ws.users.infrastructure.service;

import com.appsdevelloperblog.reactive.ws.users.infrastructure.data.UserEntity;
import com.appsdevelloperblog.reactive.ws.users.infrastructure.data.UserRepository;
import com.appsdevelloperblog.reactive.ws.users.infrastructure.presentation.CreateUserRequest;
import com.appsdevelloperblog.reactive.ws.users.infrastructure.presentation.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserRest> createUser(Mono<CreateUserRequest> createUserRequestMono) {

        // create UserEntity object
        return createUserRequestMono
                .map(this::convertToEntity)
                .flatMap(userRepository::save)
                .map(this::convertToRest)
                .onErrorMap(throwable -> {
                    if (throwable instanceof ResponseStatusException ){
                        return new ResponseStatusException(HttpStatus.CONFLICT, throwable.getMessage());
                    } else if (throwable instanceof DataIntegrityViolationException ){
                        return new ResponseStatusException(HttpStatus.BAD_REQUEST, throwable.getMessage());
                    }
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage());
                });
    }

    @Override
    public Mono<UserRest> getUserById(UUID id) {
        return userRepository.findById(id)
                .mapNotNull(this::convertToRest);
    }

    @Override
    public Flux<UserRest> findAll(int page, int limit) {
        if (page >0) page = page - 1 ;
        Pageable pageable = PageRequest.of(page, limit);
        return userRepository.findAllBy(pageable)
                .map(this::convertToRest);
    }

    private UserEntity convertToEntity(CreateUserRequest createUserRequest){

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(createUserRequest, userEntity);

        return userEntity;

    }

    private UserRest convertToRest(UserEntity userEntity){

        UserRest userRest = new UserRest();
        BeanUtils.copyProperties(userEntity, userRest);

        return userRest;

    }
}
