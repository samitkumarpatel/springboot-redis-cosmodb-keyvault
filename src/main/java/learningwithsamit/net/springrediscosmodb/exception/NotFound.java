package learningwithsamit.net.springrediscosmodb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not Found")
public class NotFound extends RuntimeException {
	public NotFound() {
		super();
	}
	public NotFound(String msg) {
		super(msg);
	}
}
