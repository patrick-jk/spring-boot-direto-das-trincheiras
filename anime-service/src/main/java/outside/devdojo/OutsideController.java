package outside.devdojo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OutsideController {

    @GetMapping(value = "test")
    public String test() {
        return "OMAE WA MOU SHINDE IRU";
    }
}
