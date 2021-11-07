package com.exist.exercise08.services.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.exist.exercise08.model.employee.Employee;
import com.exist.exercise08.model.payload.registration.MessageResponseDto;
import com.exist.exercise08.model.ticket.Ticket;
import com.exist.exercise08.model.ticket.TicketDto;
import com.exist.exercise08.services.data.EmployeeRepository;
import com.exist.exercise08.services.data.TicketRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/ticket")
public class TicketController {
    
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

        if(!getAssignedEmployee.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee id " +
                 " for assignee " + ticket.getAssignedEmployeeId() + " does not exist");
        }
        System.out.println("!!!!!TEST ADDED !!!!!");
        
        //If watchers emp-id has an input
        if(ticket.getWatchersEmployeeId() != null){
            Set<Employee> employeeWatchers = new HashSet<Employee>();
            ticket.getWatchersEmployeeId()
                .stream()
                .forEach(id->{
                    Optional<Employee> watcherEmployee = employeeRepo.findById(id);
                    if(!watcherEmployee.isPresent()){
                        throw new ResponseStatusException
                            (HttpStatus.NOT_FOUND, "Employee id " + id + " does not exist");
                    }else{
                        employeeWatchers.add(watcherEmployee.get());
                    }
                });

            //Add the Watchers
            employeeWatchers.stream().forEach((employee)->{
                saveTicket.addWatcher(employee);
            });

            // saveTicket.getWatchers().add(getEmployeeWatchers.get());
            // saveTicket.addWatcher(employeeWatchers);
        }        
        
        saveTicket.setAssignedEmployee(getAssignedEmployee.get());
 
        //Persist to database        
        ticketRepo.save(saveTicket);

        //Persist to database        
        return ResponseEntity.ok(saveTicket);
    }

    @GetMapping("/get-ticket-by-id")
    public ResponseEntity<?> getTicketById(Long id) {
        Optional<Ticket> getTicket = ticketRepo.findById(id);
        
        if(!getTicket.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Ticket id " + id + " does not exist");
        }
        return ResponseEntity.ok(getTicket.get());
    }

    @GetMapping("/get-assigned-employee-by-id")
    public ResponseEntity<?> getAssignedEmployeeById(Long id){
        Optional<Ticket> getTicket = ticketRepo.findById(id);

        if(!getTicket.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Ticket id " + id + " does not exist");
        }
        Employee employee = new Employee();
        employee.setId(getTicket.get().getAssignedEmployee().getId());
        employee.setFirstName(getTicket.get().getAssignedEmployee().getFirstName());
        employee.setMiddleName(getTicket.get().getAssignedEmployee().getMiddleName());
        employee.setLastName(getTicket.get().getAssignedEmployee().getLastName());
        employee.setDepartment(getTicket.get().getAssignedEmployee().getDepartment());
        employee.setAssignedTickets(getTicket.get().getAssignedEmployee().getAssignedTickets());
        employee.setTicketsWatched(getTicket.get().getAssignedEmployee().getTicketsWatched());

        return ResponseEntity.ok(employee);
    }

    @GetMapping("/get-watchers-by-id")
    public ResponseEntity<?> getWatchersById(Long id){
        Optional<Ticket> getTicket = ticketRepo.findById(id);

        if(!getTicket.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Ticket id " + id + " does not exist");
        }

        List<Employee> employeeWatchers = new ArrayList<Employee>(); 
       
        for (Employee em : getTicket.get().getWatchers()) {
            Employee employee = new Employee();
            employee.setId(em.getId());
            employee.setFirstName(em.getFirstName());
            employee.setMiddleName(em.getMiddleName());
            employee.setLastName(em.getLastName());
            employee.setDepartment(em.getDepartment());
            employee.setAssignedTickets(em.getAssignedTickets());
            employee.setTicketsWatched(em.getTicketsWatched());
            employeeWatchers.add(employee);
        }

        return ResponseEntity.ok(employeeWatchers);        
    }


    @GetMapping("/get-ticket-list")
    public ResponseEntity<List<Ticket>> getTicketList(){
        List<Ticket> ticketLists= (List<Ticket>) ticketRepo.findAll();

        if(ticketLists.isEmpty()){
            throw new ResponseStatusException
                (HttpStatus.NO_CONTENT, "There are no current tickets");
        }

        return ResponseEntity.ok(ticketLists);
    }

    //TODO - CHANGE ASSIGNED PERSON AND ADD MULTIPLE WATCHERS
    @PutMapping("/update-ticket-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateTicketById(@RequestBody TicketDto ticketNewValue, Long id){
        Optional<Ticket> ticketToUpdate = ticketRepo.findById(id);
        Optional<Employee> findEmployee = employeeRepo.findById(ticketNewValue.getAssignedEmployeeId());

        if(!ticketToUpdate.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Ticket id " + id + " does not exist");
        }

        if(!findEmployee.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee id " + id + " does not exist");
        }

        if(StringUtils.isBlank(ticketNewValue.getTitle())
            || StringUtils.isBlank(ticketNewValue.getDescription())
            || ticketNewValue.getSeverity() == null
            || ticketNewValue.getStatus() == null){
                throw new ResponseStatusException
                (HttpStatus.BAD_REQUEST, "Inputs must not be empty");
        }

        ticketToUpdate.get().setTitle(ticketNewValue.getTitle());
        ticketToUpdate.get().setDescription(ticketNewValue.getDescription());
        ticketToUpdate.get().setSeverity(ticketNewValue.getSeverity());
        ticketToUpdate.get().setStatus(ticketNewValue.getStatus());
        ticketToUpdate.get().setAssignedEmployee(findEmployee.get());
        
        Set<Employee> watchers = new HashSet<Employee>();
        ticketNewValue.getWatchersEmployeeId()
            .stream()
            .forEach((employee)->{
                Optional<Employee> watcherEmployee = employeeRepo.findById(id);
                if(!watcherEmployee.isPresent()){
                    throw new ResponseStatusException
                        (HttpStatus.NOT_FOUND, "Employee id " + id + " does not exist");
                }else{
                    watchers.add(watcherEmployee.get());
                }
            });

        // watchers.add(employeeRepo.findById(ticketNewValue.getWatchersEmployeeId()).get());
        ticketToUpdate.get().setWatchers(watchers);
        ticketRepo.save(ticketToUpdate.get());
        return ResponseEntity.ok(new MessageResponseDto("Ticket updated successfully!"));
    }

    @DeleteMapping("/delete-ticket-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTicketById(Long id){

        Optional<Ticket> ticketToDelete = ticketRepo.findById(id);

        // for(Ticket ticket: ticketToDelete.get().getAssignedEmployee().getTicketsWatched()){
        //     // ticket.get
        //     // ticket.removeWatcher(ticket.getAssignedEmployee());
        //     // ticket.getAssignedEmployee().removeAssignedTicket(ticket);
        //     ticketToDelete.get().removeWatcher(ticket.getAssignedEmployee());
        //     ticketToDelete.get().removeAssignedEmployee(ticket);
        //     // ticket.removeWatcher(ticket.getAssignedEmployee());
        // }
        ticketToDelete.get().getAssignedEmployee().removeAssignedTicket(ticketToDelete.get());
        ticketToDelete.get().getWatchers().clear();
        // ticketRepo.delete(ticketToDelete.get());
        // ticketRepo.deleteById(ticketToDelete.get().getId());
        ticketRepo.delete(ticketToDelete.get());
        return ResponseEntity.ok(new MessageResponseDto("Ticket deleted successfully!"));
    }    


}
