package learningwithsamit.net.springrediscosmodb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class AuditLog {
	@Id
	private UUID id;
	private Date date;
	private Entity entity;
	private Operation operationType;
	private String user;

}
