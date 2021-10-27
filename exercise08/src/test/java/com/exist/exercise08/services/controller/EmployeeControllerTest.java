package com.exist.exercise08.services.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.exist.exercise08.model.employee.Department;
import com.exist.exercise08.model.employee.Employee;
import com.exist.exercise08.services.data.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeControllerTest{
    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    ObjectMapper mapper;

    @MockBean
    EmployeeRepository employeeRepo;

    Employee EMPLOYEE_1 = new Employee("John", "Doe", "Doo", Department.IT);
    Employee EMPLOYEE_2 = new Employee("lobo", "Doe", "Doo", Department.ADMIN);
    Employee EMPLOYEE_3 = new Employee("looni", "Doe", "Doo", Department.HR);

    @Test
    public void test_get_all_data_employee_data() throws Exception{
        List<Employee> employeeList = new ArrayList<>(Arrays.asList(EMPLOYEE_1, EMPLOYEE_2, EMPLOYEE_3));

        Mockito.when(employeeRepo.findAll()).thenReturn(employeeList);

        mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/employee/get-employee-list")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$", hasSize(3)))
                            .andExpect(jsonPath("$[0].firstName", is("John")))                       
        ;
    }


}
