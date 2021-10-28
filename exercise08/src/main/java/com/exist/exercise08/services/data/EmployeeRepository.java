package com.exist.exercise08.services.data;

import com.exist.exercise08.model.employee.Employee;


import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository <Employee,Long> {

 }
