package com.exist.exercise08.services.data;

import java.util.List;
import java.util.Set;

import com.exist.exercise08.model.employee.Employee;
import com.exist.exercise08.model.ticket.Ticket;

import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository <Employee,Long> {
    // List<Employee> findAssignedEmployeeByTicketId(Long id);

 }
