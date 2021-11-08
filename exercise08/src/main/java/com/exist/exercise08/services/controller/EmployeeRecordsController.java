package com.exist.exercise08.services.controller;

import java.util.List;
import java.util.Optional;

import com.exist.exercise08.model.employee.AddAssignedTicketDto;
import com.exist.exercise08.model.employee.Employee;
import com.exist.exercise08.model.employee.EmployeeDto;
import com.exist.exercise08.model.payload.registration.MessageResponseDto;
import com.exist.exercise08.model.ticket.Ticket;
import com.exist.exercise08.services.data.EmployeeRepository;
import com.exist.exercise08.services.data.TicketRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/employee")
public class EmployeeRecordsController {
    
    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private TicketRepository ticketRepo;

    @PostMapping("/create-employee")
    public ResponseEntity<?> createNewEmployeeDetails(@RequestBody EmployeeDto employee){

        if(   StringUtils.isBlank(employee.getFirstName()) 
           || StringUtils.isBlank(employee.getMiddleName())
           || StringUtils.isBlank(employee.getLastName()) 
           || employee.getDepartment() == null){
                throw new ResponseStatusException
                    (HttpStatus.BAD_REQUEST, "Name and Department must not be blank");
        } 
        Employee saveNewEmployee = new Employee(employee.getFirstName()
            , employee.getMiddleName(), employee.getLastName(), employee.getDepartment());
    
        return ResponseEntity.ok(employeeRepo.save(saveNewEmployee));
    }

    @GetMapping("/get-employee-by-id")
    public ResponseEntity<?> getEmployeeById(Long id) {
        Optional<Employee> getEmployee = employeeRepo.findById(id);

        // EmployeeDisplay displayEmployee = new EmployeeDisplay(getEmployee.get().getId()
        // , getEmployee.get().getFirstName(), getEmployee.get().getMiddleName() ,
        // getEmployee.get().getLastName(), getEmployee.get().getDepartment(), 
        // getEmployee.get().getAssignedTickets(), getEmployee.get().getTicketsWatched());

        if(!getEmployee.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee id " + id + " does not exist");
        }
        return ResponseEntity.ok(getEmployee);
    }

    @GetMapping("/get-employee-list")
    public ResponseEntity<List<Employee>> getEmployeeList(){
        List<Employee> employeeList = (List<Employee>) employeeRepo.findAll();
        if(employeeList.isEmpty()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee list is currently empty");
        }
        return ResponseEntity.ok((List<Employee>) employeeRepo.findAll());
    }

    @PutMapping("/update-employee-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateEmployeeById(@RequestBody EmployeeDto employeeNewValue, Long id){
        Optional<Employee> employeeToUpdate = employeeRepo.findById(id);

        if(!employeeToUpdate.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee id " + id + " does not exist");
        }

        //Checks for blank update input of names and department
        if(   StringUtils.isBlank(employeeNewValue.getFirstName()) 
            || StringUtils.isBlank(employeeNewValue.getMiddleName())
            || StringUtils.isBlank(employeeNewValue.getLastName()) 
            || employeeNewValue.getDepartment() == null){
                throw new ResponseStatusException
                    (HttpStatus.BAD_REQUEST, "Name and Department must not be blank");
        } 
        
        employeeToUpdate.get().setFirstName(employeeNewValue.getFirstName());
        employeeToUpdate.get().setMiddleName(employeeNewValue.getMiddleName());
        employeeToUpdate.get().setLastName(employeeNewValue.getLastName());
        employeeToUpdate.get().setDepartment(employeeNewValue.getDepartment());
        employeeRepo.save(employeeToUpdate.get());
        return ResponseEntity.ok(new MessageResponseDto("Employee updated successfully!"));
    }

    @PostMapping("/add-assigned-ticket")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addAssignedTicketToEmployee(@RequestBody AddAssignedTicketDto addAssignedTicketDto){
        Optional<Employee> employee = employeeRepo.findById(addAssignedTicketDto.getEmployeeId());
        Optional<Ticket> ticketAssigned = ticketRepo.findById(addAssignedTicketDto.getTicketIdAssigned());


        if(!employee.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee id " + addAssignedTicketDto.getEmployeeId() + " does not exist");
        }

        if(!ticketAssigned.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Ticket id " + addAssignedTicketDto.getTicketIdAssigned() + " does not exist");
        }

        if(employee.get().getAssignedTickets().contains(ticketAssigned.get())){
            throw new ResponseStatusException
             (HttpStatus.NOT_FOUND, "Ticket id " + addAssignedTicketDto.getTicketIdAssigned() + " already assigned");
        }

        employee.get().getAssignedTickets().add(ticketAssigned.get());

        return ResponseEntity.ok(new MessageResponseDto("Ticket Assigned successfully changed"));
    }

    @DeleteMapping("/remove-ticket-watched-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeTicketWatched(Long employeeId, Long ticketIdWatched){
        Optional<Employee> employee = employeeRepo.findById(employeeId);
        Optional<Ticket> ticketWatched = ticketRepo.findById(ticketIdWatched);


        if(!employee.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee id " + employeeId + " does not exist");
        }

        if(!ticketWatched.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Watched Ticket id " + ticketIdWatched + " does not exist");
        }

        ticketWatched.get().removeWatcher(employee.get());
        // employee.get().removeTicketsWatched(ticketWatched.get());
        employeeRepo.save(employee.get());

        // employee.get().removeTicketsWatched(ticketWatched.get());
        // ticketRepo.save(ticketWatched.get());

        return ResponseEntity.ok(new MessageResponseDto("Ticket watched successfully removed"));
    }

    @DeleteMapping("/remove-assigned-ticket-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeAssignedTicket(Long employeeId, Long ticketIdWatched){
        Optional<Employee> employee = employeeRepo.findById(employeeId);
        Optional<Ticket> ticketAssigned = ticketRepo.findById(ticketIdWatched);

        if(!employee.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee id " + employeeId + " does not exist");
        }

        if(!ticketAssigned.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Watched Ticket id " + ticketIdWatched + " does not exist");
        }

        // ticketAssigned.get().removeAssignedEmployee(ticket);
        employee.get().getAssignedTickets().remove(ticketAssigned.get());
        ticketAssigned.get().setAssignedEmployee(null);

        employeeRepo.save(employee.get());

        // ticketAssigned.get().setAssignedEmployee(null);
        // ticketRepo.save(ticketAssigned.get());

        return ResponseEntity.ok(new MessageResponseDto("Assigned Ticket successfully deleted"));

    }

    @DeleteMapping("/delete-employee-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEmployeeById(Long id){
        Optional<Employee> employeeToDelete = employeeRepo.findById(id);

        if(!employeeToDelete.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee id " + id + " does not exist");
        }

        for(Ticket ticket: employeeToDelete.get().getAssignedTickets()){
            employeeToDelete.get().removeAssignedTicket(ticket);
            employeeToDelete.get().removeTicketsWatched(ticket);
        }

        //Safely delete the obj
        employeeRepo.deleteById(employeeToDelete.get().getId());
        return ResponseEntity.ok(new MessageResponseDto("Employee deleted successfully!"));
    }

}
