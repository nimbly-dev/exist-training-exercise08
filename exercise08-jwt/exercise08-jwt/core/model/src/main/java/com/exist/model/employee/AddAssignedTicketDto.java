package com.exist.model.employee;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddAssignedTicketDto {

    @NotBlank
    private Long employeeId;
    @NotBlank
    private Long ticketIdAssigned;
}
