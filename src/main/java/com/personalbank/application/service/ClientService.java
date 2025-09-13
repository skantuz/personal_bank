package com.personalbank.application.service;

import com.personalbank.application.dto.ClientDto;
import com.personalbank.domain.model.Client;
import com.personalbank.domain.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientDto createClient(ClientDto clientDto) {
        validateClientDto(clientDto);
        
        if (clientRepository.existsByEmail(clientDto.getEmail())) {
            throw new IllegalArgumentException("Client with email " + clientDto.getEmail() + " already exists");
        }

        Client client = mapToEntity(clientDto);
        Client savedClient = clientRepository.save(client);
        return mapToDto(savedClient);
    }

    @Transactional(readOnly = true)
    public Optional<ClientDto> getClientById(Long id) {
        return clientRepository.findById(id).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Optional<ClientDto> getClientByEmail(String email) {
        return clientRepository.findByEmail(email).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClientDto> searchClientsByFirstName(String firstName) {
        return clientRepository.findByFirstNameContainingIgnoreCase(firstName).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClientDto> searchClientsByLastName(String lastName) {
        return clientRepository.findByLastNameContainingIgnoreCase(lastName).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ClientDto updateClient(Long id, ClientDto clientDto) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client with id " + id + " not found"));

        // Check if email is being changed and if the new email already exists
        if (!existingClient.getEmail().equals(clientDto.getEmail()) && 
            clientRepository.existsByEmail(clientDto.getEmail())) {
            throw new IllegalArgumentException("Client with email " + clientDto.getEmail() + " already exists");
        }

        existingClient.setFirstName(clientDto.getFirstName());
        existingClient.setLastName(clientDto.getLastName());
        existingClient.setEmail(clientDto.getEmail());
        existingClient.setPhoneNumber(clientDto.getPhoneNumber());
        existingClient.setAddress(clientDto.getAddress());

        Client updatedClient = clientRepository.save(existingClient);
        return mapToDto(updatedClient);
    }

    public void deleteClient(Long id) {
        if (!clientRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Client with id " + id + " not found");
        }
        clientRepository.deleteById(id);
    }

    private void validateClientDto(ClientDto clientDto) {
        if (clientDto == null) {
            throw new IllegalArgumentException("Client data cannot be null");
        }
        if (clientDto.getFirstName() == null || clientDto.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (clientDto.getLastName() == null || clientDto.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (clientDto.getEmail() == null || clientDto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (clientDto.getAddress() == null || clientDto.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }
    }

    private Client mapToEntity(ClientDto dto) {
        Client client = new Client();
        client.setId(dto.getId());
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setEmail(dto.getEmail());
        client.setPhoneNumber(dto.getPhoneNumber());
        client.setAddress(dto.getAddress());
        return client;
    }

    private ClientDto mapToDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setPhoneNumber(client.getPhoneNumber());
        dto.setAddress(client.getAddress());
        return dto;
    }
}