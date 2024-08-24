package esfe.org.Prueba23.controladores;

import esfe.org.Prueba23.modelos.Producto;
import esfe.org.Prueba23.modelos.Marca;
import esfe.org.Prueba23.servicios.interfaces.IProductoService;
import esfe.org.Prueba23.servicios.interfaces.IMarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;
    @Autowired
    private IMarcaService marcaService; 
    


    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Producto> productoPage = productoService.buscarTodosPaginados(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("productos", productoPage);

        int totalPages = productoPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "productos/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("marcas", marcaService.obtenerTodos());

        List<Producto> productos = productoService.obtenerTodos();
    List<Marca> marcas = marcaService.obtenerTodos();

    model.addAttribute("productos", productos);
    model.addAttribute("marcas", marcas);
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("producto") Producto producto, RedirectAttributes redirectAttributes) {
        productoService.crearOEditar(producto);
        redirectAttributes.addFlashAttribute("msg", "Producto guardado exitosamente!");
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Producto producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
            List<Producto> productos = productoService.obtenerTodos();
            List<Marca> marcas = marcaService.obtenerTodos();

                model.addAttribute("productos", productos);
            model.addAttribute("marcas", marcas);

            return "productos/edit"; 
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("producto") Producto producto, RedirectAttributes redirectAttributes) {
        productoService.crearOEditar(producto);
        redirectAttributes.addFlashAttribute("msg", "Producto actualizado exitosamente!");
        return "redirect:/productos";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id, Model model) {
        Producto producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
        return "productos/delete"; // Asegúrate de que este archivo exista
    }

    @PostMapping("/delete")
public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes) {
    productoService.eliminarPorId(id);
    redirectAttributes.addFlashAttribute("msg", "Producto eliminado exitosamente!");
    return "redirect:/productos";
}


    @GetMapping("/details/{id}")
public String details(@PathVariable Long id, Model model) {
    Producto producto = productoService.buscarPorId(id);
    model.addAttribute("producto", producto);
    return "productos/details"; 
}
    }

