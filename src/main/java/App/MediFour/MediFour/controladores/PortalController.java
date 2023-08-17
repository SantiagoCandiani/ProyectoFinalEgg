package App.MediFour.MediFour.controladores;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author scand
 */
@Controller
@Slf4j
public class PortalController {

    @GetMapping("/")
    public String index() {
       
    
        return "index.html";
    }
}
