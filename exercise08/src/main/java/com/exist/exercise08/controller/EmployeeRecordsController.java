package com.exist.exercise08.controller;

import java.util.List;
import java.util.Optional;

import com.exist.exercise08.model.employee.AddAssignedTicketDto;
import com.exist.exercise08.model.employee.Employee;
import com.exist.exercise08.model.employee.EmployeeDto;
import com.exist.exercise08.model.payload.registration.MessageResponseDto;
import com.exist.exercise08.model.ticket.Ticket;
import com.exist.exercise08.services.EmployeeService;
import com.exist.exercise08.services.TicketService;
import com.exist.exercise08.services.data.EmployeeRepository;
import com.exist.exercise08.services.data.TicketRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/employee")
public class EmployeeRecordsController {
    
    @Autowired
    TicketService ticketService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private TicketRepository ticketRepo;

    @PostMapping("/create-employee")
    public ResponseEntity<?> createNewEmployeeDetails(@RequestBody EmployeeDto employee){
        employeeService.checkIfEmployeeDtoIsBlank(employee);
    
        return ResponseEntity.ok(employeeService.createNewEmployee(employee));
    }

    @GetMapping("/get-employee-by-id")
    public ResponseEntity<?> getEmployeeById(Long id) {
        Optional<Employee> getEmployee = employeeRepo.findById(id);
        employeeService.checkIfEmployeeIsExisting(getEmployee, id);

        return ResponseEntity.ok(getEmployee);
    }

    @GetMapping("/get-employee-list")
    public ResponseEntity<List<Employee>> getEmployeeList(){
        List<Employee> employeeList = (List<Employee>) employeeRepo.findAll();
        employeeService.checkIfEmployeeListIsEmpty(employeeList);

        return ResponseEntity.ok((List<Employee>) employeeRepo.findAll());
    }

    @PutMapping("/update-employee-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateEmployeeById(@RequestBody EmployeeDto employeeNewValue, Long id){
        Optional<Employee> employeeToUpdate = employeeRepo.findById(id);

        employeeService.checkIfEmployeeIsExisting(employeeToUpdate, id);

        //Checks for blank update input of names and department
        employeeService.checkIfEmployeeDtoIsBlank(employeeNewValue);
        employeeService.updateEmployee(employeeToUpdate, employeeNewValue);

        return ResponseEntity.ok(new MessageResponseDto("Employee updated successfully!"));
    }

    @PostMapping("/add-assigned-ticket")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addAssignedTicketToEmployee(@RequestBody AddAssignedTicketDto addAssignedTicketDto){
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

    @DeleteMapping("/remove-assigned-ticket-by-id")
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

}
