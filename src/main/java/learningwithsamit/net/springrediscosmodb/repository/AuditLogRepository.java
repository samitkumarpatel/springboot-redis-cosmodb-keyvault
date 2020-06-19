package learningwithsamit.net.springrediscosmodb.repository;

import learningwithsamit.net.springrediscosmodb.model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, UUID> {}
