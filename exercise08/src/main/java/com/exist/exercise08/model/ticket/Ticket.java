package com.exist.exercise08.model.ticket;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


import com.exist.exercise08.model.employee.Employee;

import org.springframework.lang.Nullable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ticket")
@Data
@NoArgsConstructor
public class Ticket {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ticket_number")
    private Long ticketNumber;

    @NotNull
    @Setter
    private String title;

    @NotNull
    @Setter
    private String description;

    @Enumerated(EnumType.STRING)
    @Setter
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Setter
    private Status status;

    //TODO - CHANGE FIELD NAME TO ASSIGNED EMPLOYEE
    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY
                , optional = true)
    @Setter
    private Employee employee;

    @OneToMany(mappedBy = "ticket")
    @Nullable
    private List<Employee> watchers;
}
