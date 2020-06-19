package learningwithsamit.net.springrediscosmodb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Employee {
	@Id
	private UUID id = UUID.randomUUID();
	private String name;
	private String address;
	private Department department;
}
