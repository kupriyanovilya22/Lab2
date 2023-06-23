package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	private final EmployeeRepository employeeRepository;

	@Autowired
	public EmployeeController(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@GetMapping("/healthcheck")
	public String getHealthCheck() {
		return "{ \"isWorking\" : true }";
	}

	@GetMapping
	public List<Employee> getEmployees() {
		List<Employee> employeesList = new ArrayList<>();
		employeeRepository.findAll().forEach(employeesList::add);
		return employeesList;
	}

	@GetMapping("/{id}")
	public Optional<Employee> getEmployee(@PathVariable String id) {
		return employeeRepository.findById(id);
	}

	@PutMapping("/{id}")
	public Optional<Employee> updateEmployee(
			@RequestBody Employee updatedEmployee,
			@PathVariable String id
	) {
		Optional<Employee> optionalEmp = employeeRepository.findById(id);
		if (optionalEmp.isPresent()) {
			Employee employee = optionalEmp.get();
			employee.setFirstName(updatedEmployee.getFirstName());
			employee.setLastName(updatedEmployee.getLastName());
			employee.setEmail(updatedEmployee.getEmail());
			employeeRepository.save(employee);
		}
		return optionalEmp;
	}

	@DeleteMapping("/{id}")
	public String deleteEmployee(@PathVariable String id) {
		boolean exists = employeeRepository.existsById(id);
		employeeRepository.deleteById(id);
		return "{ \"success\" : " + exists + " }";
	}

	@PostMapping
	public Employee addEmployee(@RequestBody Employee newEmployee) {
		String id = String.valueOf(new Random().nextInt());
		Employee employee = new Employee(
				id,
				newEmployee.getFirstName(),
				newEmployee.getLastName(),
				newEmployee.getEmail()
		);
		employeeRepository.save(employee);
		return employee;
	}
}

