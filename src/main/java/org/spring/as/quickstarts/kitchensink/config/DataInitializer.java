package org.spring.as.quickstarts.kitchensink.config;

import org.spring.as.quickstarts.kitchensink.model.Member;
import org.spring.as.quickstarts.kitchensink.data.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;

    @Autowired
    public DataInitializer(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) {
        // Check if the database is empty
        if (memberRepository.count() == 0) {
            // Create and save the initial member
            Member johnSmith = new Member();
            johnSmith.setName("John Smith");
            johnSmith.setEmail("john.smith@mailinator.com");
            johnSmith.setPhoneNumber("2125551212");
            
            memberRepository.save(johnSmith);
            System.out.println("Initial member 'John Smith' has been added to the database.");
        }
    }
} 