package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import com.example.demo.Repository.Entity.Ticket;

public interface TicketRepository extends ListCrudRepository<Ticket, Integer> {


}
