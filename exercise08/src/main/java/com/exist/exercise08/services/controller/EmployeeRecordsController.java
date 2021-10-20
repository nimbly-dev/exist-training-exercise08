package com.exist.exercise08.services.controller;

import java.util.List;
import java.util.Optional;


import com.exist.exercise08.model.employee.Employee;
import com.exist.exercise08.services.data.EmployeeRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


//TODO - ADD VALIDATION TO ENDPOINTS
@RestController
public class EmployeeRecordsController {
    
    @Autowired
    private EmployeeRepository employeeRepo;

    @PostMapping("/create-employee")
    public Employee createNewEmployeeDetails(@RequestBody Employee employee){
        //TODO - ADD VALIDATION GUARD FOR NULL DEPARTMENT
        if(   StringUtils.isBlank(employee.getFirstName()) 
           || StringUtils.isBlank(employee.getMiddleName())
           || StringUtils.isBlank(employee.getLastName()) ){
                throw new ResponseStatusException
                    (HttpStatus.BAD_REQUEST, "Name and Department must not be blank");
        } 
        return employeeRepo.save(employee);
    }

    @GetMapping("/get-employee-by-id")
    public Optional<Employee> getEmployeeById(Long id) {
        Optional<Employee> getEmployee = employeeRepo.findById(id);
        if(!getEmployee.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee id " + id + " does not exist");
        }
        return getEmployee;
    }

    @GetMapping("/get-employee-list")
    public List<Employee> getEmployeeList(){
        List<Employee> employeeList = (List<Employee>) employeeRepo.findAll();
        if(employeeList.isEmpty()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee list is currently empty");
        }
        return (List<Employee>) employeeRepo.findAll();
    }

    @PutMapping("/update-employee-by-id")
    public Employee updateEmployeeById(@RequestBody Employee employeeNewValue, Long id){
        Optional<Employee> employeeToUpdate = employeeRepo.findById(id);

        if(!employeeToUpdate.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee id " + id + " does not exist");
        }

        //Checks for blank update input of names and department
        //TODO - ADD VALIDATION GUARD FOR NULL DEPARTMENT
        if(   StringUtils.isBlank(employeeNewValue.getFirstName()) 
            || StringUtils.isBlank(employeeNewValue.getMiddleName())
            || StringUtils.isBlank(employeeNewValue.getLastName()) ){
                throw new ResponseStatusException
                    (HttpStatus.BAD_REQUEST, "Name and Department must not be blank");
        } 
        
        employeeToUpdate.get().setFirstName(employeeNewValue.getFirstName());
        employeeToUpdate.get().setFirstName(employeeNewValue.getMiddleName());
        employeeToUpdate.get().setFirstName(employeeNewValue.getLastName());
        return employeeRepo.save(employeeToUpdate.get());
    }

    @DeleteMapping("/delete-employee-by-id")
    public void deleteEmployeeById(Long id){
        Optional<Employee> employeeToDelete = employeeRepo.findById(id);

        if(!employeeToDelete.isPresent()){
            throw new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Employee id " + id + " does not exist");
        }

        employeeRepo.deleteById(employeeToDelete.get().getEmployeeId());
    }

}
