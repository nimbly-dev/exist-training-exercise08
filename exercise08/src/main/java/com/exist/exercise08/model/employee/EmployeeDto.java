package com.exist.exercise08.model.employee;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    
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

    @Nullable
    private Long ticketId;
}
