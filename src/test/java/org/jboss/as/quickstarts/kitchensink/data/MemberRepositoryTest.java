package org.jboss.as.quickstarts.kitchensink.data;

import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @Autowired
    private MemberRepository memberRepository;

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
