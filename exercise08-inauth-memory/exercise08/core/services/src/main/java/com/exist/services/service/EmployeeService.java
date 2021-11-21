package com.exist.services.service;

import com.exist.model.employee.Employee;
import com.exist.model.employee.EmployeeDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.exist.dao.repository.EmployeeRepository;
import com.exist.model.ticket.Ticket;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepo;

    public void checkIfEmployeeDtoIsBlank( EmployeeDto employee) {
        if(    StringUtils.isBlank(employee.getFirstName()) 
            || StringUtils.isBlank(employee.getMiddleName())
            || StringUtils.isBlank(employee.getLastName()) 
            || employee.getDepartment() == null){
                throw new ResponseStatusException
                    (HttpStatus.BAD_REQUEST, "Name or Department must not be blank");
        } 
    }

    
    public Employee createNewEmployee(EmployeeDto employee){
        Employee saveNewEmployee = new Employee(employee.getFirstName()
            , employee.getMiddleName(), employee.getLastName(), employee.getDepartment());

        return employeeRepo.save(saveNewEmployee);
    }

    public void checkIfEmployeeIsExisting(Optional<Employee> employee, Long selectedId){
        if(!employee.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee id " + selectedId + " does not exist");
        }
    }

    public void checkIfEmployeeListIsEmpty(List<Employee> employeeList){
        if(employeeList.isEmpty()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee list is currently empty");
        }
    }

    public void checkIfTicketIsAlreadyAssigned(Optional<Employee> employee,  
        Optional<Ticket> ticketAssigned, Long ticketIdToBeAssigned){
        if(employee.get().getAssignedTickets().contains(ticketAssigned.get())){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Ticket id " + ticketIdToBeAssigned + " already assigned");
        }
    }
    
    @Transactional
    public void removeReferencedEmployeeRelationships(Optional<Employee> employeeToDelete){
        for(Ticket ticket: employeeToDelete.get().getAssignedTickets()){
            employeeToDelete.get().removeAssignedTicket(ticket);
            employeeToDelete.get().removeTicketsWatched(ticket);
        }
    }
    
    @Transactional //ROLLBACK ON FAIL e.g throws exception
    public void updateEmployee(Optional<Employee> employeeToUpdate,EmployeeDto employeeNewValue){
        employeeToUpdate.get().setFirstName(employeeNewValue.getFirstName());
        employeeToUpdate.get().setMiddleName(employeeNewValue.getMiddleName());
        employeeToUpdate.get().setLastName(employeeNewValue.getLastName());
        employeeToUpdate.get().setDepartment(employeeNewValue.getDepartment());

        employeeRepo.save(employeeToUpdate.get());
    }
  
}
