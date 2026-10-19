package com.appsdevelloperblog.reactive.ws.users.presentation;

import com.appsdevelloperblog.reactive.ws.users.service.UserService;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(UserController.class)
class UserControllerTest {

    @MockitoBean
    private UserService userService;

}