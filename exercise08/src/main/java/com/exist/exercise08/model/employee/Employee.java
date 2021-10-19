package com.exist.exercise08.model.employee;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
public class Employee {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
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
    private Department department;


    
}
