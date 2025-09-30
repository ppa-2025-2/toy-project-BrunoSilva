package com.example.demo.Controller.DTO;

import java.util.List;

public record NewTicketDTO(
        Integer criador,
        Integer destinatario,
        String objeto,
        String acao,
        String detalhes,
        String local,
        List<Integer> observadores
) {}
