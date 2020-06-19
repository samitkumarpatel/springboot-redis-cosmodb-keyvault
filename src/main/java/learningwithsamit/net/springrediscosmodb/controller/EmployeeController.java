package learningwithsamit.net.springrediscosmodb.controller;

import learningwithsamit.net.springrediscosmodb.exception.NotFound;
import learningwithsamit.net.springrediscosmodb.model.ApplicationDetails;
import learningwithsamit.net.springrediscosmodb.model.Employee;
import learningwithsamit.net.springrediscosmodb.model.Entity;
import learningwithsamit.net.springrediscosmodb.model.Operation;
import learningwithsamit.net.springrediscosmodb.repository.EmployeeRepository;
import learningwithsamit.net.springrediscosmodb.service.AuditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class EmployeeController {

	@Value("${app.slot:Development}")
	private String slot;

	@Autowired
    EmployeeRepository employeeRepository;

	@Autowired
	AuditorService auditorService;

	@GetMapping("/api")
    ResponseEntity<ApplicationDetails> getIndex() {
		return ResponseEntity.ok().body(new ApplicationDetails("PONG",getClass().getPackage().getImplementationVersion(),slot));
	}

	@GetMapping("/api/employee")
    Page<Employee> getAll(Pageable pageable) {
		return employeeRepository.findAll(pageable).map( employee ->  {
			return employee;
		});
	}

	@GetMapping("/api/employee/{id}")
	Employee get(@PathVariable("id") UUID id) {
		return employeeRepository.findById(id).map( emp -> {
			return emp;
		}).orElseThrow( () -> new NotFound("Employee Not Found"));
	}

	@PostMapping("/api/employee")
	Employee post(@RequestBody Employee employee) {
		auditorService.audit(Entity.EMPLOYEE, Operation.CREATE);
		return employeeRepository.save(employee);
	}

	@PutMapping("/api/employee/{id}")
	Employee post(@PathVariable("id") UUID id, @RequestBody Employee employee) {
		return employeeRepository.findById(id).map( emp -> {
			emp.setName(employee.getName());
			emp.setAddress(employee.getAddress());
			emp.setDepartment(employee.getDepartment());
			auditorService.audit(Entity.EMPLOYEE,Operation.UPDATE);
			return employeeRepository.save(emp);
		}).orElseThrow(()-> new NotFound("Employee Id is not available"));
	}

	@DeleteMapping("/api/employee/{id}")
	ResponseEntity<?> delete(@PathVariable("id") UUID id) {

		return employeeRepository.findById(id).map( emp -> {
			employeeRepository.delete(emp);
			auditorService.audit(Entity.EMPLOYEE,Operation.DELETE);
			return ResponseEntity.ok().build();
		}).orElseThrow(()-> new NotFound("Employee Id is not available"));
	}
}
