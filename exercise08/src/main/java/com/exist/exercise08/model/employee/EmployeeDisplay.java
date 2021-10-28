package com.exist.exercise08.model.employee;

import java.util.HashSet;
import java.util.Set;

import com.exist.exercise08.model.ticket.Ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDisplay {

    private Long id;

    private String firstName;
    private String middleName;
    private String lastName;

    private Department department;


    private Set<Ticket> assignedTickets;

    Set<Ticket> ticketsWatched = new HashSet<>();


}
