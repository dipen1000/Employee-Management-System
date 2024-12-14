package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.model.Employee;
import com.example.employeemanagementsystem.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class EmployeeController {
	
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    @GetMapping("/")
    public String aboutPage() {
        return "Frontend/home_page"; 
    }

    @GetMapping("/employee_list")
    public String home(@RequestParam(value = "name", defaultValue = "") String name, Model model) {

        List<Employee> employeeList = employeeService.getAllEmployees();

        model.addAttribute("employeeList", employeeList);
        return "employee_list";
    }

    @GetMapping("create")
    public String create(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "create_employee";
    }

    @PostMapping("save")
    public String save(@Valid @ModelAttribute("employee") Employee employee, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            return "/create_employee";
        }

        employeeService.save(employee);

        redirectAttributes.addFlashAttribute("message", "Employee saved successfully");
        return "redirect:/employee_list";
    }

    @GetMapping("/employee/{id}")
    public String employee(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.findById(id);

        employee.ifPresent(value -> model.addAttribute("employee", value));

        return "show_employee";
    }

    @GetMapping("employee/{id}/edit")
    public String edit(@PathVariable long id, Model model) {
    	Employee employee = employeeService.findById(id).orElse(null);
        model.addAttribute("employee", employee);
        return "create_employee";
    }

    @GetMapping("employee/{id}/delete")
    public String delete(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
    	employeeService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Employee deleted successfully");
        return "redirect:/employee_list";
    }
}
