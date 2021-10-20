package com.exist.exercise08.model.employee;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.exist.exercise08.model.ticket.Ticket;

import org.springframework.lang.Nullable;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
public class Employee {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "employee_id")
    private Long employeeId;

    @NotNull
    @Size(min=2, message="first name must be at least 2 characters long")
    @Setter
    private String firstName;

    @NotNull
    @Size(min=2, message="middle name must be at least 2 characters long")
    @Setter
    private String middleName;

    @NotNull
    @Size(min=2, message="last name must be at least 2 characters long")
    @Setter
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Setter
    @NotNull
    private Department department;

    //TODO
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_number")
    @Nullable
    private Ticket ticket;

}
