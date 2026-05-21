package com.appsdevelloperblog.reactive.ws.users.service;

import com.appsdevelloperblog.reactive.ws.users.data.UserEntity;
import com.appsdevelloperblog.reactive.ws.users.data.UserRepository;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final UserRepository userRepository;

    public AuthenticationServiceImpl(ReactiveAuthenticationManager reactiveAuthenticationManager, UserRepository userRepository) {
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<Map<String, String>> authenticate(String username, String password) {
        return reactiveAuthenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password))
                .then(getUserDetails(username)
                .map(this::createAuthenticationResponse));
    }

    private Map<String, String> createAuthenticationResponse(UserEntity user) {
        Map<String, String> result = new HashMap<>();
        result.put("userId", user.getId().toString());
        result.put("token", "JWT"); // replace it later with jwt
        return result;
    }

    private Mono<UserEntity> getUserDetails(String username) {
        return userRepository.findByEmail(username);
    }
}
