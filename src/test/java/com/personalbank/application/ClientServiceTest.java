package com.personalbank.application;

import com.personalbank.application.dto.ClientDto;
import com.personalbank.application.service.ClientService;
import com.personalbank.domain.model.Client;
import com.personalbank.domain.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private ClientDto clientDto;
    private Client client;

    @BeforeEach
    void setUp() {
        clientDto = new ClientDto("John", "Doe", "john.doe@example.com", "+1234567890", "123 Main St");
        client = new Client("John", "Doe", "john.doe@example.com", "+1234567890", "123 Main St");
        client.setId(1L);
    }

    @Test
    void testCreateClient() {
        when(clientRepository.existsByEmail(anyString())).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientDto result = clientService.createClient(clientDto);

        assertNotNull(result);
        assertEquals(clientDto.getFirstName(), result.getFirstName());
        assertEquals(clientDto.getLastName(), result.getLastName());
        assertEquals(clientDto.getEmail(), result.getEmail());
        verify(clientRepository).existsByEmail(clientDto.getEmail());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void testCreateClientWithExistingEmail() {
        when(clientRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> clientService.createClient(clientDto));
        verify(clientRepository).existsByEmail(clientDto.getEmail());
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void testGetClientById() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Optional<ClientDto> result = clientService.getClientById(1L);

        assertTrue(result.isPresent());
        assertEquals(client.getFirstName(), result.get().getFirstName());
        verify(clientRepository).findById(1L);
    }

    @Test
    void testGetClientByIdNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ClientDto> result = clientService.getClientById(1L);

        assertFalse(result.isPresent());
        verify(clientRepository).findById(1L);
    }

    @Test
    void testGetAllClients() {
        Client client2 = new Client("Jane", "Smith", "jane.smith@example.com", "+0987654321", "456 Oak Ave");
        client2.setId(2L);
        List<Client> clients = Arrays.asList(client, client2);

        when(clientRepository.findAll()).thenReturn(clients);

        List<ClientDto> result = clientService.getAllClients();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(clientRepository).findAll();
    }

    @Test
    void testUpdateClient() {
        ClientDto updateDto = new ClientDto("John", "Updated", "john.updated@example.com", "+1111111111", "New Address");
        
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.existsByEmail("john.updated@example.com")).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientDto result = clientService.updateClient(1L, updateDto);

        assertNotNull(result);
        verify(clientRepository).findById(1L);
        verify(clientRepository).existsByEmail("john.updated@example.com");
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void testDeleteClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        assertDoesNotThrow(() -> clientService.deleteClient(1L));

        verify(clientRepository).findById(1L);
        verify(clientRepository).deleteById(1L);
    }

    @Test
    void testDeleteClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> clientService.deleteClient(1L));
        verify(clientRepository).findById(1L);
        verify(clientRepository, never()).deleteById(1L);
    }
}