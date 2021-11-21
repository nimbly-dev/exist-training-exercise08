package com.exist.dao.data;

import com.exist.model.employee.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository <Employee,Long> {
 }
