package org.spring.as.quickstarts.kitchensink.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.spring.as.quickstarts.kitchensink.model.Member;
import org.spring.as.quickstarts.kitchensink.data.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@Configuration
@Profile("dev")
public class DataInitializer {

    @Autowired
    private MemberRepository memberRepository;

    @Bean
    public CommandLineRunner initData(MongoTemplate mongoTemplate) {
        return args -> {
            // Drop existing collection
            mongoTemplate.dropCollection("members");

            // Load initial data
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ClassPathResource resource = new ClassPathResource("data/members.json");
            List<Member> members = mapper.readValue(resource.getInputStream(), new TypeReference<List<Member>>() {});
            memberRepository.saveAll(members);
        };
    }
} 