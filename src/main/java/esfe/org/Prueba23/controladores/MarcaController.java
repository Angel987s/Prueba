package esfe.org.Prueba23.controladores;

import esfe.org.Prueba23.modelos.Marca;
import esfe.org.Prueba23.servicios.interfaces.IMarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/marcas")
public class MarcaController {

    @Autowired
  private IMarcaService marcaService;

  @GetMapping
     public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
         int currentPage = page.orElse(1) - 1;
         int pageSize = size.orElse(5);
         Pageable pageable = PageRequest.of(currentPage, pageSize);

         Page<Marca> marcas = marcaService.buscarTodosPaginados(pageable);
         model.addAttribute("marcas", marcas);

         int totalPages = marcas.getTotalPages();
         if (totalPages > 0) {
             List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
         }

     return "marcas/index";
    }

  @GetMapping("/create")
  public String create(Model model) {
    model.addAttribute("marca", new Marca());
    return "marcas/create";
  }

  @PostMapping("/save")
  public String save(@ModelAttribute("marca") Marca marca, BindingResult result, Model model, RedirectAttributes attributes) {
    if (result.hasErrors()) {
      model.addAttribute("marca", marca);  

      attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
      return "marcas/create";
    }

    marcaService.crearOEditar(marca);
    attributes.addFlashAttribute("msg", "Marca creada correctamente");
    return "redirect:/marcas";
  }

  @GetMapping("/details/{id}")
  public String details(@PathVariable("id") Long id, Model model) {
    Optional<Marca> marca = marcaService.buscarPorId(id);
    if (marca.isPresent()) {
      model.addAttribute("marca", marca.get());
      return "marcas/details";
    } else {
      return "redirect:/marcas";
    }
  }

  @GetMapping("/edit/{id}")
  public String edit(@PathVariable("id") Long id, Model model) {
    Optional<Marca> marca = marcaService.buscarPorId(id);
    if (marca.isPresent()) {
      model.addAttribute("marca", marca.get());
      return "marcas/edit";
    } else {
      return "redirect:/marcas";
    }
  }

  @PostMapping("/update")
  public String update(@ModelAttribute("marca") Marca marca, BindingResult result, Model model, RedirectAttributes attributes) {
    if (result.hasErrors()) {
      model.addAttribute("marca", marca);
      return  
 "marcas/edit";
    }

    marcaService.crearOEditar(marca);
    attributes.addFlashAttribute("msg", "Marca actualizada correctamente");
    return "redirect:/marcas";
  }

  @GetMapping("/remove/{id}")
  public String remove(@PathVariable("id") Long id, Model model) {
    Optional<Marca> marca = marcaService.buscarPorId(id);
    if (marca.isPresent()) {
      model.addAttribute("marca", marca.get());
      return "marcas/delete";
    } else {
      return "redirect:/marcas";
    }
  }

  @PostMapping("/delete")
  public String delete(@RequestParam("id") Long id, RedirectAttributes attributes) {
    marcaService.eliminarPorId(id);
    attributes.addFlashAttribute("msg", "Marca eliminada correctamente");
    return "redirect:/marcas";
  }
}
