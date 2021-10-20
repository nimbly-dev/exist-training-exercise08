package com.exist.exercise08.services.data;

import com.exist.exercise08.model.ticket.Ticket;

import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket,Long> {
    
}
