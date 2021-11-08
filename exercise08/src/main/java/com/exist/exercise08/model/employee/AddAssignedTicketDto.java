package com.exist.exercise08.model.employee;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddAssignedTicketDto {

    @NotBlank
    private Long employeeId;
    @NotBlank
    private Long ticketIdAssigned;
}
