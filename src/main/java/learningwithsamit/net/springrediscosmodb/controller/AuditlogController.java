package learningwithsamit.net.springrediscosmodb.controller;

import learningwithsamit.net.springrediscosmodb.model.AuditLog;
import learningwithsamit.net.springrediscosmodb.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditlogController {
	@Autowired
    AuditLogRepository auditLogRepository;

	@GetMapping("/api/auditlog")
    Page<AuditLog> getAll(Pageable pageable) {
		return auditLogRepository.findAll(pageable);
	}
}
