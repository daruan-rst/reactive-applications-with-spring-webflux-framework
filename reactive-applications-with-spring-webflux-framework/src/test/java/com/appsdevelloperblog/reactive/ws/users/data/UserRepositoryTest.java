package com.appsdevelloperblog.reactive.ws.users.data;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.UUID;

@DataR2dbcTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    @Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void setUp() {
        UserEntity user1 = new UserEntity(UUID.randomUUID(),
                "john",
                "doe",
                "john.doe@gmail.com",
                "123456789");

        UserEntity user2 = new UserEntity(UUID.randomUUID(),
                "jane",
                "doe",
                "jane.doe@gmail.com",
                "123456789");

        String insertSql = "INSERT INTO users (id, first_name, last_name, email, password) " +
                "VALUES (:id, :firstName, :lastName, :email, :password)";

        Flux.just(user1, user2)
                .concatMap(user -> databaseClient.sql(insertSql)
                .bind("id", user.getId())
                .bind("firstName", user.getFirstName())
                .bind("lastName", user.getLastName())
                .bind("email", user.getEmail())
                .bind("password", user.getPassword())
                .fetch()
                .rowsUpdated())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @AfterAll
    void tearDown() {
        databaseClient.sql("TRUNCATE TABLE users")
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void test_findByEmail_WithEmailThatExists_ReturnMatchingUser() {

        // Arrange
        String emailToFind = "john.doe@gmail.com";

        // Act and Assert
        StepVerifier.create(userRepository.findByEmail(emailToFind))
                .expectNextMatches(user -> user.getEmail().equals(emailToFind))
                .verifyComplete();
    }
}