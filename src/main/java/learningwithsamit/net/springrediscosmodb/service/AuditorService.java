package learningwithsamit.net.springrediscosmodb.service;

import learningwithsamit.net.springrediscosmodb.model.AuditLog;
import learningwithsamit.net.springrediscosmodb.model.Entity;
import learningwithsamit.net.springrediscosmodb.model.Operation;
import learningwithsamit.net.springrediscosmodb.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AuditorService {
	@Autowired
    AuditLogRepository auditLogRepository;

	public void audit(Entity entity, Operation operation) {
		auditLogRepository.save(AuditLog.builder().id(UUID.randomUUID()).date(new Date()).entity(entity).operationType(operation).user("UNKNOWN").build());
	}
}
