package com.exist.model.employee;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddAssignedTicketDto {

    @NotBlank(message = "Must not be blank")
    private Long employeeId;

    @NotBlank(message = "Must not be blank")
    private Long ticketIdAssigned;
}
