package com.exist.dao.repository;

import com.exist.model.employee.Employee;
import org.springframework.data.repository.CrudRepository;
import com.exist.model.ticket.Ticket;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket,Long> {
    
    List<Ticket> findByAssignedEmployee(Employee assignedEmployee);
    // List<Ticket> findAssignedEmployeeById(Long id);
}
