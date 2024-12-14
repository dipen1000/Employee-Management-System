package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.model.Employee;
import com.example.employeemanagementsystem.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
public class EmployeeApiController {
	private final EmployeeService employeeService;

    public EmployeeApiController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Get All Employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // Get Employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.findById(id);
        return employee.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    // Create New Employee
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
    	Employee savedEmployee = employeeService.save(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    // Update Existing Employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
        @PathVariable Long id, 
        @Valid @RequestBody Employee employeeDetails
    ) {
        Optional<Employee> existingEmployee= employeeService.findById(id);
        
        if (existingEmployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Employee employeeToUpdate = existingEmployee.get();
        
        // Update fields - adjust these based on your employee model
        employeeToUpdate.setFirstName(employeeDetails.getFirstName());
        employeeToUpdate.setLastName(employeeDetails.getLastName());
        // Add other fields as necessary

        Employee updatedEmployee = employeeService.save(employeeToUpdate);
        return ResponseEntity.ok(updatedEmployee);
    }

    // Delete Employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.findById(id);
        
        if (employee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        employeeService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Global Exception Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExceptions(Exception e) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("An error occurred: " + e.getMessage());
    }
}
