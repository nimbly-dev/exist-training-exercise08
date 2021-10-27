package com.exist.exercise08.services.controller;

import java.util.List;
import java.util.Optional;

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


//TODO - ADD VALIDATION TO ENDPOINTS
//TODO - TEST ENDPOINTS ON POSTMAN
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

        Optional<Employee> getEmployee = employeeRepo.findById(ticket.getAssignedEmployeeId());
        
        if(!getEmployee.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee id " + ticket.getAssignedEmployeeId() + " does not exist");
        }
        
        saveTicket.setAssignedEmployee(getEmployee.get());

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
        return ResponseEntity.ok(getTicket);
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
            || ticketNewValue.getStatus() == null)

        ticketToUpdate.get().setDescription(ticketNewValue.getDescription());
        ticketToUpdate.get().setTitle(ticketNewValue.getTitle());
        ticketToUpdate.get().setSeverity(ticketNewValue.getSeverity());
        ticketToUpdate.get().setStatus(ticketNewValue.getStatus());
        ticketToUpdate.get().setAssignedEmployee(findEmployee.get());
        // ticketToUpdate.get().setWatchers(ticketNewValue.getWatchers());
        ticketRepo.save(ticketToUpdate.get());
        return ResponseEntity.ok(new MessageResponseDto("Ticket updated successfully!"));
    }

    @DeleteMapping("/delete-ticket-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTicketById(Long id){
        ticketRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponseDto("Ticket deleted successfully!"));
    }    


}
