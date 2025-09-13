package com.personalbank.domain;

import com.personalbank.domain.model.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void testClientCreation() {
        Client client = new Client("John", "Doe", "john.doe@example.com", "+1234567890", "123 Main St");
        
        assertEquals("John", client.getFirstName());
        assertEquals("Doe", client.getLastName());
        assertEquals("john.doe@example.com", client.getEmail());
        assertEquals("+1234567890", client.getPhoneNumber());
        assertEquals("123 Main St", client.getAddress());
        assertEquals("John Doe", client.getFullName());
        assertNotNull(client.getCreatedAt());
        assertNull(client.getUpdatedAt());
    }

    @Test
    void testClientEquality() {
        Client client1 = new Client("John", "Doe", "john.doe@example.com", "+1234567890", "123 Main St");
        Client client2 = new Client("Jane", "Smith", "john.doe@example.com", "+0987654321", "456 Oak Ave");
        
        client1.setId(1L);
        client2.setId(1L);
        
        assertEquals(client1, client2); // Same ID and email
        assertEquals(client1.hashCode(), client2.hashCode());
    }

    @Test
    void testClientToString() {
        Client client = new Client("John", "Doe", "john.doe@example.com", "+1234567890", "123 Main St");
        client.setId(1L);
        
        String toString = client.toString();
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("Doe"));
        assertTrue(toString.contains("john.doe@example.com"));
    }
}