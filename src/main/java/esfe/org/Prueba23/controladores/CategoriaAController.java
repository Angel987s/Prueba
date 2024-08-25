package esfe.org.Prueba23.controladores;

import esfe.org.Prueba23.modelos.CategoriaA;
import esfe.org.Prueba23.servicios.interfaces.ICategoriaAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categorias")
public class CategoriaAController {

    @Autowired
    private ICategoriaAService categoriaService;

    @GetMapping
    public String index(Model model) {
        List<CategoriaA> categorias = categoriaService.listarTodas();
        model.addAttribute("categorias", categorias);
        return "categorias/index";
    }

    
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("categoria", new CategoriaA());
        return "categorias/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("categoria") CategoriaA categoria, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("categoria", categoria);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "categorias/create";
        }

        categoriaService.guardar(categoria);
        attributes.addFlashAttribute("msg", "Categoría creada correctamente");
        return "redirect:/categorias";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Optional<CategoriaA> categoria = categoriaService.obtenerPorId(id);
        if (categoria.isPresent()) {
            model.addAttribute("categoriaA", categoria.get());
            return "categorias/details";
        } else {
            return "redirect:/categorias";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Optional<CategoriaA> categoriaA = categoriaService.obtenerPorId(id);
        if (categoriaA.isPresent()) {
            model.addAttribute("categoriaA", categoriaA);
            return "categorias/edit";
        } else {
            return "redirect:/categorias";
        }
    }

   


    @PostMapping("/update")
    public String update(@ModelAttribute("categoria") CategoriaA categoria, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("categoria", categoria);
            return "categorias/edit";
        }

        categoriaService.guardar(categoria);
        attributes.addFlashAttribute("msg", "Categoría actualizada correctamente");
        return "redirect:/categorias";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Optional<CategoriaA> categoria = categoriaService.obtenerPorId(id);
        if (categoria.isPresent()) {
            model.addAttribute("categoriaA", categoria.get());
            return "categorias/delete";
        } else {
            return "redirect:/categorias";
        }
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Integer id, RedirectAttributes attributes) {
        categoriaService.eliminar(id);
        attributes.addFlashAttribute("msg", "Categoría eliminada correctamente");
        return "redirect:/categorias";
    }
}
