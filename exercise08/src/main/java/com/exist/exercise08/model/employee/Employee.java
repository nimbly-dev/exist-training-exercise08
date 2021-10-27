package com.exist.exercise08.model.employee;

import java.io.Serializable;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.exist.exercise08.model.ticket.Ticket;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.springframework.lang.Nullable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
public class Employee implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;

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
    @OneToMany(mappedBy = "assignedEmployee")
    @JsonIgnore
    @Nullable
    private Set<Ticket> assignedTickets;


    public Employee(String firstName,String middleName,String lastName,
            Department department) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.department = department;
    }

    

}
