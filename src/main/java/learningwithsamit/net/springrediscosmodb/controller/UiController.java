package learningwithsamit.net.springrediscosmodb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {
	@GetMapping("/")
	public String index() {
		return "index";
	}
}
