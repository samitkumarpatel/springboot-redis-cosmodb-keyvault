package learningwithsamit.net.springrediscosmodb.controller;

import learningwithsamit.net.springrediscosmodb.exception.NotFound;
import learningwithsamit.net.springrediscosmodb.model.Department;
import learningwithsamit.net.springrediscosmodb.model.Entity;
import learningwithsamit.net.springrediscosmodb.model.Operation;
import learningwithsamit.net.springrediscosmodb.repository.DepartmentRepository;
import learningwithsamit.net.springrediscosmodb.service.AuditorService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DepartmentController {

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
		auditorService.audit(Entity.DEPARTMENT, Operation.CREATE);
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
