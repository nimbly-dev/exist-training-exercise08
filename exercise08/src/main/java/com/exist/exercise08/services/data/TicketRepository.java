package com.exist.exercise08.services.data;

import java.util.Optional;

import com.exist.exercise08.model.ticket.Ticket;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket,Long> {
    Ticket getAssignedEmployeeById(Long id);
}
