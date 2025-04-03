package org.jboss.as.quickstarts.kitchensink.service;

import org.jboss.as.quickstarts.kitchensink.model.Member;
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
        // Act
        memberRegistration.register(testMember);

        // Assert
        verify(eventPublisher).publishEvent(testMember);
    }

    @Test
    void register_EventPublishingFails_ThrowsException() {
        // Arrange
        doThrow(new RuntimeException("Publishing failed"))
                .when(eventPublisher).publishEvent(any(Member.class));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            memberRegistration.register(testMember);
        });

        assertEquals("Publishing failed", exception.getMessage());
    }

    @Test
    void register_NullMember_ThrowsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            memberRegistration.register(null);
        });

        verify(eventPublisher, never()).publishEvent(any());
    }
}
