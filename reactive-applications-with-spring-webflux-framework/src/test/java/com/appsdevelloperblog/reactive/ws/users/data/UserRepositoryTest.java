package com.appsdevelloperblog.reactive.ws.users.data;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;

@DataR2dbcTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    @BeforeAll
    void setUp() {
    }

    @AfterAll
    void tearDown() {
    }

    @Test
    void findByEmail() {
    }
}