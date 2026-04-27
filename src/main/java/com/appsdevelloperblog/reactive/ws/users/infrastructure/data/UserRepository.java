package com.appsdevelloperblog.reactive.ws.users.infrastructure.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveCrudRepository <UserEntity, UUID>{
}
