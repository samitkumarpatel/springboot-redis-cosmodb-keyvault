package learningwithsamit.net.springrediscosmodb.repository;

import learningwithsamit.net.springrediscosmodb.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, UUID> {}
