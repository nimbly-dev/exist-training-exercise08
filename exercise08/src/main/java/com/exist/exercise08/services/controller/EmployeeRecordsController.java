package com.exist.exercise08.services.controller;

import java.util.List;
import java.util.Optional;

import com.exist.exercise08.model.employee.Employee;
import com.exist.exercise08.services.data.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeRecordsController {
    
    @Autowired
    private EmployeeRepository employeeRepo;

    @PostMapping("/create-employee")
    public Employee createNewEmployeeDetails(@RequestBody Employee employee){
        return employeeRepo.save(employee);
    }

    @GetMapping("/get-employee-by-id")
    public Optional<Employee> getEmployeeById(Long id){
        return employeeRepo.findById(id);
    }

    @GetMapping("/get-employee-list")
    public List<Employee> getEmployeeList(){
        return (List<Employee>) employeeRepo.findAll();
    }

    @PutMapping("/update-employee-by-id")
    public Employee updateEmployeeById(@RequestBody Employee employeeNewValue, Long id){
        Optional<Employee> employeeToUpdate = employeeRepo.findById(id);
        employeeToUpdate.get().setFirstName(employeeNewValue.getFirstName());
        employeeToUpdate.get().setFirstName(employeeNewValue.getMiddleName());
        employeeToUpdate.get().setFirstName(employeeNewValue.getLastName());
        return employeeRepo.save(employeeToUpdate.get());
    }

    @DeleteMapping("/delete-employee-by-id")
    public void deleteEmployeeById(Long id){
        employeeRepo.deleteById(id);
    }

}
