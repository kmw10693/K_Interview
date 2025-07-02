package backend.k_interview.global.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue interviewReservedQueue() {
        return new Queue("interview.reserved", true);
    }
}
