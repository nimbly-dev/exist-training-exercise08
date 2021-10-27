package com.exist.exercise08.model.ticket;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketDto {
    @NotBlank
    @Size(min = 2)
    private String title;

    @NotBlank
    @Size(min = 2)
    private String description;

    @Enumerated(EnumType.STRING)
    @NotBlank
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @NotBlank
    private Status status;

    @NotBlank
    private Long assignedEmployeeId;
}
