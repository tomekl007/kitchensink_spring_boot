package org.jboss.as.quickstarts.kitchensink.service;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberRegistrationTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberRegistration memberRegistration;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member();
        testMember.setName("John Doe");
        testMember.setEmail("john@example.com");
        testMember.setPhoneNumber("1234567890");
    }

    @Test
    void register_SuccessfulRegistration_PublishesEvent() throws Exception {
        // Arrange
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        // Act
        memberRegistration.register(testMember);

        // Assert
        verify(memberRepository).save(testMember);
        verify(eventPublisher).publishEvent(testMember);
    }

    @Test
    void register_EventPublishingFails_ThrowsException() {
        // Arrange
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);
        doThrow(new RuntimeException("Publishing failed"))
                .when(eventPublisher).publishEvent(any(Member.class));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            memberRegistration.register(testMember);
        });

        assertEquals("Publishing failed", exception.getMessage());
        verify(memberRepository).save(testMember);
    }

    @Test
    void register_NullMember_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            memberRegistration.register(null);
        });

        verify(memberRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }
}
