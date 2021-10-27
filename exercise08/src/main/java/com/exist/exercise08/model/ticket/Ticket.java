package com.exist.exercise08.model.ticket;

import java.io.Serializable;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.exist.exercise08.model.employee.Employee;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ticket")
@Data
@RequiredArgsConstructor
public class Ticket implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "assignee_id", referencedColumnName = "employee_id")
    @Setter
    private Employee assignedEmployee;

    // @OneToMany(mappedBy = "ticket")
    // @Nullable
    // private List<Employee> watchers;


    public Ticket(String title, String description, Severity severity, Status status) {
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.status = status;
    }

    
}
