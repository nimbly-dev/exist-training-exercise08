package com.exist.api.controller;


import com.exist.model.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.exist.model.payload.registration.MessageResponseDto;
import com.exist.dao.repository.EmployeeRepository;
import com.exist.dao.repository.TicketRepository;
import com.exist.services.service.EmployeeService;
import com.exist.services.service.TicketService;
import com.exist.model.ticket.Ticket;
import com.exist.model.ticket.TicketDto;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/ticket")
public class TicketController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private TicketRepository ticketRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @PostMapping("/create-com.exist.model.ticket")
    public ResponseEntity<Ticket> createNewTicket(@Valid @RequestBody TicketDto ticket){
        Ticket saveTicket = new Ticket(
            ticket.getTitle(), ticket.getDescription(), ticket.getSeverity(), 
            ticket.getStatus());

        Optional<Employee> getAssignedEmployee = employeeRepo.findById(ticket.getAssignedEmployeeId());

        ticketService.checkIfTicketDtoIsBlank(ticket);
        employeeService.checkIfEmployeeIsExisting(getAssignedEmployee, ticket.getAssignedEmployeeId());
        
        ticketService.saveTicket(ticket, saveTicket, getAssignedEmployee);

        return ResponseEntity.ok(saveTicket);
    }

    @GetMapping("/get-com.exist.model.ticket-by-id")
    public ResponseEntity<?> getTicketById(Long id) {
        Optional<Ticket> getTicket = ticketRepo.findById(id);
        
        ticketService.checkTicketIfExisting(getTicket, id);

        return ResponseEntity.ok(getTicket.get());
    }


    @GetMapping("/get-assigned-com.exist.model.employee-by-id")
    public ResponseEntity<?> getAssignedEmployeeById(Long id){
        Optional<Ticket> getTicket = ticketRepo.findById(id);

        ticketService.checkTicketIfExisting(getTicket, id);

        Employee employee = new Employee();
        ticketService.setAssignedEmployeeDto(employee, getTicket);

        return ResponseEntity.ok(employee);
    }

    @GetMapping("/get-watchers-by-id")
    public ResponseEntity<?> getWatchersById(Long id){
        Optional<Ticket> getTicket = ticketRepo.findById(id);
        ticketService.checkTicketIfExisting(getTicket, id);

        List<Employee> employeeWatchers = new ArrayList<Employee>(); 
        ticketService.setWatchersEmployeeDto(getTicket, employeeWatchers);

        return ResponseEntity.ok(employeeWatchers);        
    }


    @GetMapping("/get-ticket-list")
    public ResponseEntity<List<Ticket>> getTicketList(){
        List<Ticket> ticketLists= (List<Ticket>) ticketRepo.findAll();
        ticketService.checkIfTicketListIsEmpty(ticketLists);

        return ResponseEntity.ok(ticketLists);
    }



}
