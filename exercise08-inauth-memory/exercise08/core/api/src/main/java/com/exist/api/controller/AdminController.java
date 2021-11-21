package com.exist.api.controller;

import com.exist.dao.repository.EmployeeRepository;
import com.exist.dao.repository.TicketRepository;
import com.exist.model.employee.AddAssignedTicketDto;
import com.exist.model.employee.Employee;
import com.exist.model.employee.EmployeeDto;
import com.exist.model.payload.registration.MessageResponseDto;
import com.exist.model.ticket.Ticket;
import com.exist.model.ticket.TicketDto;
import com.exist.services.service.EmployeeService;
import com.exist.services.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    TicketService ticketService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private TicketRepository ticketRepo;

    @PutMapping("/update-employee-by-id")
    public ResponseEntity<?> updateEmployeeById(@Valid @RequestBody EmployeeDto employeeNewValue, Long id){
        Optional<Employee> employeeToUpdate = employeeRepo.findById(id);

        employeeService.checkIfEmployeeIsExisting(employeeToUpdate, id);
        employeeService.updateEmployee(employeeToUpdate, employeeNewValue);

        return ResponseEntity.ok(new MessageResponseDto("Employee updated successfully!"));
    }

    @PostMapping("/add-assigned-ticket")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addAssignedTicketToEmployee(
            @Valid @RequestBody AddAssignedTicketDto addAssignedTicketDto){
        Optional<Employee> employee = employeeRepo.findById(addAssignedTicketDto.getEmployeeId());
        Optional<Ticket> ticketAssigned = ticketRepo.findById(addAssignedTicketDto.getTicketIdAssigned());

        employeeService.checkIfEmployeeIsExisting(employee, addAssignedTicketDto.getEmployeeId());
        ticketService.checkTicketIfExisting(ticketAssigned, addAssignedTicketDto.getTicketIdAssigned());

        employeeService.checkIfTicketIsAlreadyAssigned(employee, ticketAssigned,
                addAssignedTicketDto.getTicketIdAssigned());

        employee.get().getAssignedTickets().add(ticketAssigned.get());

        employeeRepo.save(employee.get());
        return ResponseEntity.ok(new MessageResponseDto("Ticket Assigned successfully changed"));
    }

    @DeleteMapping("/remove-ticket-watched-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeTicketWatched(Long employeeId, Long ticketIdWatched){
        Optional<Employee> employee = employeeRepo.findById(employeeId);
        Optional<Ticket> ticketWatched = ticketRepo.findById(ticketIdWatched);

        employeeService.checkIfEmployeeIsExisting(employee, employeeId);
        ticketService.checkTicketIfExisting(ticketWatched, ticketIdWatched);

        ticketWatched.get().removeWatcher(employee.get());
        employeeRepo.save(employee.get());

        return ResponseEntity.ok(new MessageResponseDto("Ticket watched successfully removed"));
    }

    @DeleteMapping("/remove-ticket-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeAssignedTicket(Long employeeId, Long ticketIdWatched){
        Optional<Employee> employee = employeeRepo.findById(employeeId);
        Optional<Ticket> ticketAssigned = ticketRepo.findById(ticketIdWatched);

        employeeService.checkIfEmployeeIsExisting(employee, employeeId);
        ticketService.checkTicketIfExisting(ticketAssigned, ticketIdWatched);

        employee.get().getAssignedTickets().remove(ticketAssigned.get());
        ticketAssigned.get().setAssignedEmployee(null);

        employeeRepo.save(employee.get());

        return ResponseEntity.ok(new MessageResponseDto("Assigned Ticket successfully deleted"));

    }

    @DeleteMapping("/delete-employee-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEmployeeById(Long id){
        Optional<Employee> employeeToDelete = employeeRepo.findById(id);

        employeeService.checkIfEmployeeIsExisting(employeeToDelete, id);

        //Safely delete the obj
        employeeService.removeReferencedEmployeeRelationships(employeeToDelete);

        employeeRepo.deleteById(employeeToDelete.get().getId());
        return ResponseEntity.ok(new MessageResponseDto("Employee deleted successfully!"));
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
    public ResponseEntity<?> updateTicketById(
            @Valid @RequestBody TicketDto ticketNewValue, Long id){
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
