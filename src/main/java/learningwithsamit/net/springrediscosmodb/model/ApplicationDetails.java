package learningwithsamit.net.springrediscosmodb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDetails {
	private String ping;
	private String appVersion;
	private String slot;
}
