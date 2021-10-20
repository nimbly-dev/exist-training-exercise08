package com.exist.exercise08.services.controller;

import java.util.List;
import java.util.Optional;

import com.exist.exercise08.model.ticket.Ticket;
import com.exist.exercise08.services.data.TicketRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


//TODO - ADD VALIDATION TO ENDPOINTS
//TODO - TEST ENDPOINTS ON POSTMAN
@RestController
public class TicketController {
    
    @Autowired
    private TicketRepository ticketRepo;

    @PostMapping("/create-ticket")
    public Ticket createNewTicket(@RequestBody Ticket ticket){
        return ticketRepo.save(ticket);
    }

    @GetMapping("/get-ticket-by-id")
    public Optional<Ticket> getMethodName(Long id) {
        return ticketRepo.findById(id);
    }
    
    @GetMapping("/get-ticket-list")
    public List<Ticket> getTicketList(){
        return (List<Ticket>) ticketRepo.findAll();
    }

    @PutMapping("/update-ticket-by-id")
    public Ticket updateTicketById(@RequestBody Ticket ticketNewValue, Long id){
        Optional<Ticket> ticketToUpdate = ticketRepo.findById(id);
        ticketToUpdate.get().setDescription(ticketNewValue.getDescription());
        ticketToUpdate.get().setTitle(ticketNewValue.getTitle());
        ticketToUpdate.get().setSeverity(ticketNewValue.getSeverity());
        ticketToUpdate.get().setStatus(ticketNewValue.getStatus());
        ticketToUpdate.get().setEmployee(ticketNewValue.getEmployee());
        ticketToUpdate.get().setWatchers(ticketNewValue.getWatchers());
        return ticketRepo.save(ticketToUpdate.get());
    }

    @DeleteMapping("/delete-ticket-by-id")
    public void deleteTicketById(Long id){
        ticketRepo.deleteById(id);
    }
    
}
