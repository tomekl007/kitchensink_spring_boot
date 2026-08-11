package org.spring.as.quickstarts.kitchensink.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.spring.as.quickstarts.kitchensink.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
class MemberRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection("members");
    }

    @Test
    @DisplayName("Should save and find member by email")
    void testFindByEmail() {
        Member member = new Member();
        member.setName("Tomasz");
        member.setEmail("tomasz@example.com");
        member.setPhoneNumber("12345678932");

        memberRepository.save(member);

        Member found = memberRepository.findByEmail("tomasz@example.com");

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Tomasz");
    }

    @Test
    @DisplayName("Should return all members ordered by name")
    void testFindAllOrderedByName() {
        Member memberA = new Member();
        memberA.setName("Alice");
        memberA.setEmail("alice@example.com");
        memberA.setPhoneNumber("11111111111");

        Member memberZ = new Member();
        memberZ.setName("Zbigniew");
        memberZ.setEmail("zbigniew@example.com");
        memberZ.setPhoneNumber("99999999921");

        memberRepository.save(memberZ);
        memberRepository.save(memberA);

        List<Member> ordered = memberRepository.findAllOrderedByName();

        assertThat(ordered).hasSize(2);
        assertThat(ordered.get(0).getName()).isEqualTo("Alice");
        assertThat(ordered.get(1).getName()).isEqualTo("Zbigniew");
    }
}
