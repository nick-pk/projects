package com.app.ems.service;

import com.app.ems.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    EmployeeDto createEmployee(EmployeeDto employeeDto);

    EmployeeDto getEmployeeById(Long id);

    List<EmployeeDto> getEmployees();

    void deleteEmployee(Long id);

    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto);
}
