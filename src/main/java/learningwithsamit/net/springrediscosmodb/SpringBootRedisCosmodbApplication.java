package learningwithsamit.net.springrediscosmodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.DateUtils;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@SpringBootApplication
public class SpringBootRedisCosmodbApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRedisCosmodbApplication.class, args);
	}

}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
class Department {
	@Id
	private UUID id = UUID.randomUUID();
	private String name;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
class Employee {
	@Id
	private UUID id = UUID.randomUUID();
	private String name;
	private String address;
	private Department department;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
class AuditLog {
	@Id
	private UUID id;
	private Date date;
	private Entity entity;
	private Operation operationType;
	private String user;

}
enum Entity {
	DEPARTMENT,EMPLOYEE
}
enum Operation {
	CREATE,UPDATE,DELETE
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ApplicationDetails {
	private String ping;
	private String appVersion;
	private String slot;
}

@Repository
interface DepartmentRepository extends MongoRepository<Department,UUID> {}

@Repository
interface EmployeeRepository extends MongoRepository<Employee,UUID> {}

@Repository
interface AuditLogRepository extends MongoRepository<AuditLog,UUID> {}

@Controller
class UiController {
	@GetMapping("/")
	public String index() {
		return "index";
	}
}

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not Found")
class NotFound extends RuntimeException {
	NotFound() {
		super();
	}
	NotFound(String msg) {
		super(msg);
	}
}

@RestController
class EmployeeController {

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
		auditorService.audit(Entity.EMPLOYEE,Operation.CREATE);
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

@RestController
class DepartmentController {

	@Autowired
	DepartmentRepository departmentRepository;

	@Autowired
	AuditorService auditorService;

	@GetMapping("/api/department")
	Page<Department> getAll(Pageable pageable) {
		return departmentRepository.findAll(pageable);
	}

	@GetMapping("/api/department/{id}")
	Department get(@PathVariable("id") UUID id) {
		return departmentRepository.findById(id).map( department -> {
			return department;
		}).orElseThrow(()-> new NotFound("Not Found"));
	}

	@PostMapping("/api/department")
	Department save(@RequestBody Department department) {
		auditorService.audit(Entity.DEPARTMENT,Operation.CREATE);
		return departmentRepository.save(department);
	}

	@PutMapping("/api/department/{id}")
	Department update(@PathVariable("id") UUID id, @RequestBody Department department) {
		return departmentRepository.findById(id).map( dept -> {
			auditorService.audit(Entity.DEPARTMENT,Operation.UPDATE);
			dept.setName(department.getName());
			return departmentRepository.save(dept);
		}).orElseThrow(()-> new NotFound("Id not found"));
	}
	@DeleteMapping("/api/department/{id}")
	ResponseEntity<?> delete(@PathVariable("id") UUID id) {
		return departmentRepository.findById(id).map( dept -> {
			departmentRepository.delete(dept);
			auditorService.audit(Entity.DEPARTMENT,Operation.DELETE);
			return ResponseEntity.ok().build();
		}).orElseThrow(()-> new NotFound("Id not found"));
	}

	// Get all employee by Department Id
}

@RestController
class AuditlogController {
	@Autowired
	AuditLogRepository auditLogRepository;

	@GetMapping("/api/auditlog")
	Page<AuditLog> getAll(Pageable pageable) {
		return auditLogRepository.findAll(pageable);
	}
}

@Service
class AuditorService {
	@Autowired
	AuditLogRepository auditLogRepository;

	public void audit(Entity entity,Operation operation) {
		auditLogRepository.save(AuditLog.builder().id(UUID.randomUUID()).date(new Date()).entity(entity).operationType(operation).user("UNKNOWN").build());
	}
}