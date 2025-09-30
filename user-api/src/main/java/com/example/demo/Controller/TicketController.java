package com.example.demo.Controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Controller.DTO.NewTicketDTO;
import com.example.demo.Controller.DTO.EditTicketDTO;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.TicketRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Repository.Entity.Ticket;
import com.example.demo.Repository.Entity.User;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketController(
            TicketRepository ticketRepository,
            RoleRepository roleRepository,
            @Value("${app.user.default.roles}") Set<String> defaultRoles,
            UserRepository userRepository) {

        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public void newTicket(@RequestBody NewTicketDTO newTicket) {

        Ticket ticket = new Ticket();

        if (!userRepository.existsById(newTicket.criador())) {
            throw new IllegalArgumentException("Criador não encontrado");
        }
        if (!userRepository.existsById(newTicket.destinatario())) {
            throw new IllegalArgumentException("Destinatário não encontrado");
        }

        ticket.setCriador(userRepository.findById(newTicket.criador()).orElseThrow());
        ticket.setDestinatario(userRepository.findById(newTicket.destinatario()).orElseThrow());
        ticket.setObjeto(newTicket.objeto());
        ticket.setAcao(newTicket.acao());
        ticket.setDetalhes(newTicket.detalhes());
        ticket.setLocal(newTicket.local());

        // Observadores
        Set<User> observadores = newTicket.observadores().stream()
                .map(id -> userRepository.findById(id).orElseThrow(
                        () -> new IllegalArgumentException("Observador " + id + " não encontrado")))
                .collect(Collectors.toSet());
        ticket.setObservadores(observadores);

        ticketRepository.save(ticket);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public void editTicket(@PathVariable Integer id, @RequestBody EditTicketDTO editTicket) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket não encontrado"));

        if (ticket.getStatus() == Ticket.Status.CONCLUIDO) {
            throw new IllegalStateException("Ticket já concluído, não pode ser editado");
        }

        if (editTicket.responsavel() != null) {
            User responsavel = userRepository.findById(editTicket.responsavel())
                    .orElseThrow(() -> new IllegalArgumentException("Responsável não encontrado"));
            ticket.setResponsavel(responsavel);
        }

        if (editTicket.status() != null) {
            ticket.setStatus(editTicket.status());
        }

        ticketRepository.save(ticket);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Ticket>> getTickets() {
        return ResponseEntity.ok(ticketRepository.findAll());
    }
}
