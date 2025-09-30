package com.example.demo.Controller.DTO;

import com.example.demo.Repository.Entity.Ticket.Status;

public record EditTicketDTO(
        Integer responsavel,
        Status status
) {}
