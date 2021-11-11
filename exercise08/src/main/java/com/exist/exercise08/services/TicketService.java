package com.exist.exercise08.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.exist.exercise08.model.employee.Employee;
import com.exist.exercise08.model.ticket.Ticket;
import com.exist.exercise08.model.ticket.TicketDto;
import com.exist.exercise08.services.data.EmployeeRepository;
import com.exist.exercise08.services.data.TicketRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TicketService {

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private TicketRepository ticketRepo;

    public void checkTicketIfExisting(Optional<Ticket> ticketAssigned,Long ticketId) {
        if(!ticketAssigned.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Ticket id " + ticketId + " does not exist");
        }
    }

    public void checkIfTicketDtoIsBlank(TicketDto ticket){
        if(    StringUtils.isBlank(ticket.getTitle())
            || StringUtils.isBlank(ticket.getDescription())
            || ticket.getSeverity() == null
            || ticket.getStatus() == null){
            throw new ResponseStatusException 
                (HttpStatus.BAD_REQUEST, "Inputs must not be blank ");
        }
    }

    public void checkIfTicketListIsEmpty(List<Ticket> ticketLists){
        if(ticketLists.isEmpty()){
            throw new ResponseStatusException
                (HttpStatus.NO_CONTENT, "There are no current tickets");
        }
    }

    public void saveTicket(
            TicketDto ticket, Ticket saveTicket,
            Optional<Employee> getAssignedEmployee) {
        if(ticket.getWatchersEmployeeId() != null){
            Set<Employee> employeeWatchers = new HashSet<Employee>();
            ticket.getWatchersEmployeeId()
                .stream()
                .forEach(id->{
                    Optional<Employee> watcherEmployee = employeeRepo.findById(id);
                    if(!watcherEmployee.isPresent()){
                        throw new ResponseStatusException
                            (HttpStatus.NOT_FOUND, "Employee id " + id + " does not exist");
                    }
                    else{
                        employeeWatchers.add(watcherEmployee.get());
                    }
                });

            //Add the Watchers
            employeeWatchers.stream().forEach((employee)->{
                saveTicket.addWatcher(employee);
            });
        }      
                        
        saveTicket.setAssignedEmployee(getAssignedEmployee.get());
 
        //Persist to database        
        ticketRepo.save(saveTicket);
    }

    public void setAssignedEmployeeDto(Employee employee,  Optional<Ticket> getTicket){
        employee.setId(getTicket.get().getAssignedEmployee().getId());
        employee.setFirstName(getTicket.get().getAssignedEmployee().getFirstName());
        employee.setMiddleName(getTicket.get().getAssignedEmployee().getMiddleName());
        employee.setLastName(getTicket.get().getAssignedEmployee().getLastName());
        employee.setDepartment(getTicket.get().getAssignedEmployee().getDepartment());
    }

    public void setWatchersEmployeeDto(Optional<Ticket> getTicket, List<Employee> employeeWatchers){
        for (Employee em : getTicket.get().getWatchers()) {
            Employee employee = new Employee();
            employee.setId(em.getId());
            employee.setFirstName(em.getFirstName());
            employee.setMiddleName(em.getMiddleName());
            employee.setLastName(em.getLastName());
            employee.setDepartment(em.getDepartment());
            // employee.setAssignedTickets(em.getAssignedTickets());
            // employee.setTicketsWatched(em.getTicketsWatched());
            employeeWatchers.add(employee);
        }
    }

    public void updateTicket(Optional<Ticket> ticketToUpdate, 
        TicketDto ticketNewValue, Optional<Employee> findEmployee, Long id) {

        ticketToUpdate.get().setTitle(ticketNewValue.getTitle());
        ticketToUpdate.get().setDescription(ticketNewValue.getDescription());
        ticketToUpdate.get().setSeverity(ticketNewValue.getSeverity());
        ticketToUpdate.get().setStatus(ticketNewValue.getStatus());
        ticketToUpdate.get().setAssignedEmployee(findEmployee.get());
        
        Set<Employee> watchers = new HashSet<Employee>();
        ticketNewValue.getWatchersEmployeeId()
            .stream()
            .forEach((employee)->{
                Optional<Employee> watcherEmployee = employeeRepo.findById(id);
                if(!watcherEmployee.isPresent()){
                    throw new ResponseStatusException
                        (HttpStatus.NOT_FOUND, "Employee id " + id + " does not exist");
                }else{
                    watchers.add(watcherEmployee.get());
                }
            });

        // watchers.add(employeeRepo.findById(ticketNewValue.getWatchersEmployeeId()).get());
        ticketToUpdate.get().setWatchers(watchers);
        ticketRepo.save(ticketToUpdate.get());
    }

    public void deleteTicket(Optional<Ticket> ticketToDelete){
        ticketToDelete.get().getAssignedEmployee().removeAssignedTicket(ticketToDelete.get());
        ticketToDelete.get().getWatchers().clear();
        ticketRepo.delete(ticketToDelete.get());
    }

    public void deleteWatcherOfTicket(Optional<Ticket> ticket,Optional<Employee> employeeWatcher){
        employeeWatcher.get().removeTicketsWatched(ticket.get());
        ticket.get().removeWatcher(employeeWatcher.get());
        ticketRepo.save(ticket.get());
    }

}
