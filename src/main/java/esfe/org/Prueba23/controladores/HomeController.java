package esfe.org.Prueba23.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {
    @GetMapping("/")
    public RedirectView redirectToMarcas() {
        return new RedirectView("/productos");
    }

}