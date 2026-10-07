package com.appsdevelloperblog.reactive.ws.users.infrastructure;

import com.appsdevelloperblog.reactive.ws.users.presentation.model.UserRest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class SinksConfig {

    @Bean
    public Sinks.Many<UserRest> userSink(){
        return Sinks.many().multicast().onBackpressureBuffer();
    }
}
