package com.exist.exercise08.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class Employee {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private final Long id;

    @NotNull
    @Size(min=2, message="first name must be at least 2 characters long")
    private final String firstName;

    @NotNull
    @Size(min=2, message="middle name must be at least 2 characters long")
    private final String middleName;

    @NotNull
    @Size(min=2, message="last name must be at least 2 characters long")
    private final String lastName;

    
    public static enum DEPARTMENT{
        IT, ADMIN, HR, SALES
    }

}
