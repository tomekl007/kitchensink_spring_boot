package org.spring.as.quickstarts.kitchensink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.logging.Logger;

@SpringBootApplication
@EntityScan("org.spring.as.quickstarts.kitchensink.model")
@EnableJpaRepositories("org.spring.as.quickstarts.kitchensink.data")
public class KitchensinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(KitchensinkApplication.class, args);
    }

    @Bean
    public Logger logger() {
        return Logger.getLogger("MemberResourceRESTService");
    }
}
