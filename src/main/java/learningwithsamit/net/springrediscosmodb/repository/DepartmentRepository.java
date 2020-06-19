package learningwithsamit.net.springrediscosmodb.repository;

import learningwithsamit.net.springrediscosmodb.model.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, UUID> {}
