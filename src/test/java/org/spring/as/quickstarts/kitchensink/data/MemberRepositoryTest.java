package org.spring.as.quickstarts.kitchensink.data;

import org.spring.as.quickstarts.kitchensink.model.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class MemberRepositoryTest {

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
    @DisplayName("Should return all members ordered by name, including predefined")
    void testFindAllOrderedByName() {
        Member memberZ = new Member();
        memberZ.setName("Zbigniew");
        memberZ.setEmail("zbigniew@example.com");
        memberZ.setPhoneNumber("99999999921");

        memberRepository.save(memberZ);

        List<Member> ordered = memberRepository.findAllOrderedByName();

        assertThat(ordered).hasSizeGreaterThanOrEqualTo(1); // At least predefined + 1
        assertThat(ordered.get(0).getName()).isEqualTo("Zbigniew"); // alphabetically first
    }
}
