package com.appsdevelloperblog.reactive.ws.users.service;

import com.appsdevelloperblog.reactive.ws.users.data.UserEntity;
import com.appsdevelloperblog.reactive.ws.users.data.UserRepository;
import com.appsdevelloperblog.reactive.ws.users.presentation.CreateUserRequest;
import com.appsdevelloperblog.reactive.ws.users.presentation.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
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
                .map(this::convertToRest);
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
