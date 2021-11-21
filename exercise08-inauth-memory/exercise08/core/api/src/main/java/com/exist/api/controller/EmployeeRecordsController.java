package com.exist.api.controller;

import com.exist.model.employee.AddAssignedTicketDto;
import com.exist.model.employee.Employee;
import com.exist.model.employee.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.exist.model.payload.registration.MessageResponseDto;
import com.exist.dao.repository.EmployeeRepository;
import com.exist.dao.repository.TicketRepository;
import com.exist.services.service.EmployeeService;
import com.exist.services.service.TicketService;
import com.exist.model.ticket.Ticket;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/employee")
public class EmployeeRecordsController {
    
    @Autowired
    TicketService ticketService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private TicketRepository ticketRepo;

    @PostMapping(path = "/create-employee")
    public ResponseEntity<?> createNewEmployeeDetails(@Valid @RequestBody EmployeeDto employee){
        employeeService.createNewEmployee(employee);
        return ResponseEntity.ok(new MessageResponseDto("Employee added"));
    }

    @GetMapping(path = "/get-employee-by-id")
    public ResponseEntity<?> getEmployeeById(Long id) {
        Optional<Employee> getEmployee = employeeRepo.findById(id);
        employeeService.checkIfEmployeeIsExisting(getEmployee, id);

        return ResponseEntity.ok(getEmployee);
    }

    @GetMapping("/get-employee-list")
    public ResponseEntity<List<Employee>> getEmployeeList(){
        List<Employee> employeeList = (List<Employee>) employeeRepo.findAll();
        employeeService.checkIfEmployeeListIsEmpty(employeeList);

        return ResponseEntity.ok((List<Employee>) employeeRepo.findAll());
    }

}
