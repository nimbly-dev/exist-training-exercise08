package com.exist.model.ticket;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.exist.model.employee.Employee;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ticket")
@RequiredArgsConstructor
public class Ticket implements Serializable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    @Getter
    @Setter
    private Long id;

    @NotNull
    @Getter
    @Setter
    private String title;

    @NotNull
    @Getter
    @Setter
    private String description;

    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private Status status;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id", referencedColumnName = "employee_id")
    @JsonBackReference
    private Employee assignedEmployee;

    @ManyToMany
    @JoinTable(name = "watchers" ,
           joinColumns = @JoinColumn(name = "ticket_id"),
           inverseJoinColumns = @JoinColumn(name="employee_id"))
    @JsonBackReference
    Set<Employee> watchers = new HashSet<>();


    public Ticket(String title, String description, Severity severity, Status status) {
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.status = status;
    }

    public void addWatcher(Employee employee){
        this.watchers.add(employee);
        employee.getAssignedTickets().add(this);
    }

    public void removeWatcher(Employee employee){
        this.watchers.remove(employee);
        employee.getAssignedTickets().remove(this);
    }

    public void removeAssignedEmployee(Ticket ticket){
        this.assignedEmployee.removeAssignedTicket(ticket);
        // com.exist.model.ticket.setAssignedEmployee(null);
    }

    @JsonBackReference
    public Employee getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(Employee assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    @JsonBackReference
    public Set<Employee> getWatchers() {
        return watchers;
    }

    public void setWatchers(Set<Employee> watchers) {
        this.watchers = watchers;
    }
    
    
}
