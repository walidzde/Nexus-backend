package com.ecommerce_nexus.backend.support;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class SupportTicketTest {

    @Test
    void onCreate_setsCreatedAt_andDefaultsStatusToOpen_whenStatusIsNull() {
        // Arrange
        SupportTicket ticket = new SupportTicket();
        assertNull(ticket.getStatus(), "Precondition: status should be null before onCreate");
        assertNull(ticket.getCreatedAt(), "Precondition: createdAt should be null before onCreate");

        // Act
        ticket.onCreate();

        // Assert
        assertNotNull(ticket.getCreatedAt(), "createdAt must be initialized on create");
        assertEquals(TicketStatus.OPEN, ticket.getStatus(), "Default status should be OPEN when none set");

        // Sanity: createdAt should be a timestamp close to now
        Instant now = Instant.now();
        assertTrue(!ticket.getCreatedAt().isAfter(now), "createdAt should not be in the future");
    }

    @Test
    void onCreate_keepsExistingStatus_ifAlreadySet() {
        // Arrange
        SupportTicket ticket = new SupportTicket();
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        // Act
        ticket.onCreate();

        // Assert
        assertEquals(TicketStatus.IN_PROGRESS, ticket.getStatus(), "onCreate must not override a pre-set status");
        assertNotNull(ticket.getCreatedAt(), "createdAt must be initialized on create even if status is set");
    }
}
