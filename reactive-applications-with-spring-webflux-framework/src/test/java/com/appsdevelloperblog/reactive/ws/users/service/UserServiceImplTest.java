package com.appsdevelloperblog.reactive.ws.users.service;

import com.appsdevelloperblog.reactive.ws.users.data.UserEntity;
import com.appsdevelloperblog.reactive.ws.users.data.UserRepository;
import com.appsdevelloperblog.reactive.ws.users.presentation.model.CreateUserRequest;
import com.appsdevelloperblog.reactive.ws.users.presentation.model.UserRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private WebClient webClient;

    private Sinks.Many<UserRest> usersSink;

    private UserServiceImpl userService;

    @BeforeEach
    void setup(){
        usersSink = Sinks.many().multicast().onBackpressureBuffer();
        userService = new UserServiceImpl(userRepository, passwordEncoder, usersSink, webClient);
    }


    @Test
    void testCreateUser_withValidRequest_returnsCreatedUserDetails() {
        // Arrange

        CreateUserRequest createUserRequest = new CreateUserRequest(
                "Sergey",
                "Kargopolov",
                "user@example.com",
                "12345678"
        );

        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setEmail(createUserRequest.getEmail());
        savedEntity.setFirstName(createUserRequest.getFirstName());
        savedEntity.setLastName(createUserRequest.getLastName());
        savedEntity.setPassword(createUserRequest.getPassword());

        when(passwordEncoder.encode(any())).thenReturn("passwordEncoded");
        when(userRepository.save(any(UserEntity.class))).thenReturn(Mono.just(savedEntity));

        // Act
        Mono<UserRest> result = userService.createUser(Mono.just(createUserRequest));

        // Assert

        StepVerifier.create(result)
                .expectNextMatches(userRest ->
                        userRest.getId().equals(savedEntity.getId()) &&
                        userRest.getFirstName().equals(savedEntity.getFirstName()) &&
                        userRest.getLastName().equals(savedEntity.getLastName()) &&
                        userRest.getEmail().equals(savedEntity.getEmail())
                )
                .verifyComplete();

        verify(userRepository, times(1)).save(any(UserEntity.class));

//        UserRest user = result.block();
//        assertEquals(savedEntity.getId(), user.getId());
//        assertEquals(savedEntity.getFirstName(), user.getFirstName());
    }

    @Test
    void testCreateUser_withValidRequest_EmitsEventToSink() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("John", "Doe", "john@example.com", "password123");

        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setFirstName("John");
        savedEntity.setLastName("Doe");
        savedEntity.setEmail("john@example.com");
        savedEntity.setPassword("encodedPassword");

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(Mono.just(savedEntity));

        // Subscribe to the sink before triggering the service call.
        Flux<UserRest> sinkFlux = usersSink.asFlux();

        // Act

        // Assert
        StepVerifier.create(
                        userService.createUser(Mono.just(request))
                                .thenMany(usersSink.asFlux().take(1))
                )
                .expectNextMatches(userRest ->
                        userRest.getId().equals(savedEntity.getId()) &&
                                userRest.getFirstName().equals(savedEntity.getFirstName()) &&
                                userRest.getLastName().equals(savedEntity.getLastName()) &&
                                userRest.getEmail().equals(savedEntity.getEmail())
                )
                .verifyComplete();


    }
}