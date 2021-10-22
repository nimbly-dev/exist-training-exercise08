package com.exist.exercise08.services.controller;

import java.util.List;
import java.util.Optional;

import com.exist.exercise08.model.payload.registration.MessageResponseDto;
import com.exist.exercise08.model.ticket.Ticket;
import com.exist.exercise08.services.data.TicketRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("/create-ticket")
    public Ticket createNewTicket(@RequestBody Ticket ticket){
        return ticketRepo.save(ticket);
    }

    @GetMapping("/get-ticket-by-id")
    public ResponseEntity<?> getMethodName(Long id) {
        return ResponseEntity.ok(ticketRepo.findById(id));
    }
    
    @GetMapping("/get-ticket-list")
    public ResponseEntity<List<Ticket>> getTicketList(){
        return ResponseEntity.ok((List<Ticket>) ticketRepo.findAll());
    }

    @PutMapping("/update-ticket-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateTicketById(@RequestBody Ticket ticketNewValue, Long id){
        Optional<Ticket> ticketToUpdate = ticketRepo.findById(id);
        ticketToUpdate.get().setDescription(ticketNewValue.getDescription());
        ticketToUpdate.get().setTitle(ticketNewValue.getTitle());
        ticketToUpdate.get().setSeverity(ticketNewValue.getSeverity());
        ticketToUpdate.get().setStatus(ticketNewValue.getStatus());
        ticketToUpdate.get().setEmployee(ticketNewValue.getEmployee());
        ticketToUpdate.get().setWatchers(ticketNewValue.getWatchers());
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
