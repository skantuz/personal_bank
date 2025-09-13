package com.personalbank.infrastructure.controller;

import com.personalbank.application.dto.ClientDto;
import com.personalbank.application.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "*")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody ClientDto clientDto) {
        try {
            ClientDto createdClient = clientService.createClient(clientDto);
            return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        Optional<ClientDto> client = clientService.getClientById(id);
        return client.map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ClientDto> getClientByEmail(@PathVariable String email) {
        Optional<ClientDto> client = clientService.getClientByEmail(email);
        return client.map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping("/search/firstname/{firstName}")
    public ResponseEntity<List<ClientDto>> searchClientsByFirstName(@PathVariable String firstName) {
        List<ClientDto> clients = clientService.searchClientsByFirstName(firstName);
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping("/search/lastname/{lastName}")
    public ResponseEntity<List<ClientDto>> searchClientsByLastName(@PathVariable String lastName) {
        List<ClientDto> clients = clientService.searchClientsByLastName(lastName);
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDto clientDto) {
        try {
            ClientDto updatedClient = clientService.updateClient(id, clientDto);
            return new ResponseEntity<>(updatedClient, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}