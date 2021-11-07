package com.exist.exercise08.services.data;

import java.util.List;
import java.util.Optional;

import com.exist.exercise08.model.employee.Employee;
import com.exist.exercise08.model.ticket.Ticket;

import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket,Long> {
    
    List<Ticket> findByAssignedEmployee(Employee assignedEmployee);
    // List<Ticket> findAssignedEmployeeById(Long id);
}
