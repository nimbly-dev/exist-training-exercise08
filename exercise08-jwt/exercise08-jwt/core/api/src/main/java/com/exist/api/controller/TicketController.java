package com.exist.api.controller;

import com.exist.dao.data.EmployeeRepository;
import com.exist.dao.data.TicketRepository;
import com.exist.model.employee.Employee;
import com.exist.model.payload.registration.MessageResponseDto;
import com.exist.model.ticket.Ticket;
import com.exist.model.ticket.TicketDto;
import com.exist.services.EmployeeService;
import com.exist.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @PostMapping("/create-ticket")
    public ResponseEntity<Ticket> createNewTicket(@RequestBody TicketDto ticket){
        Ticket saveTicket = new Ticket(
            ticket.getTitle(), ticket.getDescription(), ticket.getSeverity(), 
            ticket.getStatus());

        Optional<Employee> getAssignedEmployee = employeeRepo.findById(ticket.getAssignedEmployeeId());

        ticketService.checkIfTicketDtoIsBlank(ticket);
        employeeService.checkIfEmployeeIsExisting(getAssignedEmployee, ticket.getAssignedEmployeeId());
        
        ticketService.saveTicket(ticket, saveTicket, getAssignedEmployee);

        return ResponseEntity.ok(saveTicket);
    }

    @GetMapping("/get-ticket-by-id")
    public ResponseEntity<?> getTicketById(Long id) {
        Optional<Ticket> getTicket = ticketRepo.findById(id);
        
        ticketService.checkTicketIfExisting(getTicket, id);

        return ResponseEntity.ok(getTicket.get());
    }


    @GetMapping("/get-assigned-employee-by-id")
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

    @DeleteMapping("/delete-employee-watcher")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAssignedEmployee(Long ticketId, Long employeeIdWatcherToBeDeleted){
        Optional<Ticket> ticket = ticketRepo.findById(ticketId);
        Optional<Employee> employeeWatcher = employeeRepo.findById(employeeIdWatcherToBeDeleted);

        ticketService.checkTicketIfExisting(ticket, ticketId);
        employeeService.checkIfEmployeeIsExisting(employeeWatcher, employeeIdWatcherToBeDeleted);

        ticketService.deleteWatcherOfTicket(ticket, employeeWatcher);

        return ResponseEntity.ok(new MessageResponseDto("Employee Watcher deleted successfully!"));
    }

    @PutMapping("/update-ticket-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateTicketById(@RequestBody TicketDto ticketNewValue, Long id){
        Optional<Ticket> ticketToUpdate = ticketRepo.findById(id);
        Optional<Employee> findEmployee = employeeRepo.findById(ticketNewValue.getAssignedEmployeeId());

        ticketService.checkTicketIfExisting(ticketToUpdate, id);
        employeeService.checkIfEmployeeIsExisting(findEmployee, ticketNewValue.getAssignedEmployeeId());
        ticketService.checkIfTicketDtoIsBlank(ticketNewValue);

        ticketService.updateTicket(ticketToUpdate, ticketNewValue, findEmployee, id);

        return ResponseEntity.ok(new MessageResponseDto("Ticket updated successfully!"));
    }

    @DeleteMapping("/delete-ticket-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTicketById(Long id){
        Optional<Ticket> ticketToDelete = ticketRepo.findById(id);
        ticketService.checkTicketIfExisting(ticketToDelete, id);
        ticketService.deleteTicket(ticketToDelete);

        return ResponseEntity.ok(new MessageResponseDto("Ticket deleted successfully!"));
    }    

}
