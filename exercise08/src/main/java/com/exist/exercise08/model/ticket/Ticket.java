package com.exist.exercise08.model.ticket;

import java.io.Serializable;
import java.util.HashSet;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.exist.exercise08.model.employee.Employee;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
        // ticket.setAssignedEmployee(null);
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
