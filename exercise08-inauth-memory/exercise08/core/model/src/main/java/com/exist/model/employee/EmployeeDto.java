package com.exist.model.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    

    @NotBlank(message = "First name must not be empty")
    @Size(min=2, message="first name must be at least 2 characters long")
    private String firstName;

    @Size(min=2, message="middle name must be at least 2 characters long")
    private String middleName;

    @Size(min=2, message="last name must be at least 2 characters long")
    private String lastName;

    @Enumerated(EnumType.STRING)
//    @NotBlank(message = "Department must not be empty")
    private Department department;

    @Nullable
    private Long ticketId;
}
