package com.appsdevelloperblog.reactive.ws.users.infrastructure.service;

import com.appsdevelloperblog.reactive.ws.users.infrastructure.data.UserEntity;
import com.appsdevelloperblog.reactive.ws.users.infrastructure.data.UserRepository;
import com.appsdevelloperblog.reactive.ws.users.infrastructure.presentation.CreateUserRequest;
import com.appsdevelloperblog.reactive.ws.users.infrastructure.presentation.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
