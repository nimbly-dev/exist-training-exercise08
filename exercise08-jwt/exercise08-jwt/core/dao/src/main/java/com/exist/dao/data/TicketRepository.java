package com.exist.dao.data;


import com.exist.model.employee.Employee;
import com.exist.model.ticket.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket,Long> {
    
    List<Ticket> findByAssignedEmployee(Employee assignedEmployee);
    // List<Ticket> findAssignedEmployeeById(Long id);
}
