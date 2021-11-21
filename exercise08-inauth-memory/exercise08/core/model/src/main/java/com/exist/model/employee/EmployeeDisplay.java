package com.exist.model.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.exist.model.ticket.Ticket;

import java.util.HashSet;
import java.util.Set;

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
