package com.exist.model.employee;

import com.exist.model.ticket.Ticket;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "employee")
@NoArgsConstructor
public class Employee implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "employee_id")
    @Getter
    @Setter
    private Long id;

    @NotNull
    @Size(min=2, message="first name must be at least 2 characters long")
    @Getter
    @Setter
    private String firstName;

    @NotNull
    @Size(min=2, message="middle name must be at least 2 characters long")
    @Getter
    @Setter
    private String middleName;

    @NotNull
    @Size(min=2, message="last name must be at least 2 characters long")
    @Getter
    @Setter
    private String lastName;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Getter
    @Setter
    private Department department;

    @OneToMany(mappedBy = "assignedEmployee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Nullable
    @JsonManagedReference
    private Set<Ticket> assignedTickets;

    @ManyToMany(mappedBy = "watchers", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    Set<Ticket> ticketsWatched = new HashSet<>();

    public Employee(String firstName,String middleName,String lastName,
            Department department) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.department = department;
    }


    public Employee(Long id, String firstName, String middleName, String lastName, 
        Department department, Set<Ticket> assignedTickets, Set<Ticket> ticketsWatched) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.department = department;
        this.assignedTickets = assignedTickets;
        this.ticketsWatched = ticketsWatched;
    }


    public void addTicketsWatched(Ticket ticket){
        ticketsWatched.add(ticket);
        ticket.getWatchers().add(this);
    }

    public void removeTicketsWatched(Ticket ticket){
        ticketsWatched.remove(ticket);
        ticket.getWatchers().remove(this);
    }

    public void removeAssignedTicket(Ticket ticketToDelete){
        assignedTickets.remove(ticketToDelete);
        ticketToDelete.setAssignedEmployee(null);
    }

    @JsonManagedReference
    public Set<Ticket> getAssignedTickets() {
        return assignedTickets;
    }


    public void setAssignedTickets(Set<Ticket> assignedTickets) {
        this.assignedTickets = assignedTickets;
    }

    @JsonManagedReference
    public Set<Ticket> getTicketsWatched() {
        return ticketsWatched;
    }


    public void setTicketsWatched(Set<Ticket> ticketsWatched) {
        this.ticketsWatched = ticketsWatched;
    }
    
    
}
