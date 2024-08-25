package esfe.org.Prueba23.controladores;

import esfe.org.Prueba23.modelos.ProductoA;
import esfe.org.Prueba23.servicios.interfaces.IProductoAService;
import esfe.org.Prueba23.servicios.interfaces.ICategoriaAService;
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
public class ProductoAController {

    @Autowired
    private IProductoAService productoService;

    @Autowired
    private ICategoriaAService categoriaService;

    @GetMapping
public String index(Model model, @RequestParam("page") Optional<Integer> page,
                    @RequestParam("size") Optional<Integer> size) {
    int currentPage = page.orElse(1);
    int pageSize = size.orElse(5);

    Page<ProductoA> productoPage = productoService.buscarTodosPaginados(PageRequest.of(currentPage - 1, pageSize));
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
        model.addAttribute("producto", new ProductoA());
        model.addAttribute("categorias", categoriaService.listarTodas()); // Cargar categorías para el combo
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("producto") ProductoA producto, RedirectAttributes redirectAttributes) {
        productoService.crearOEditar(producto);
        redirectAttributes.addFlashAttribute("msg", "Producto guardado exitosamente!");
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        ProductoA producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.listarTodas()); // Cargar categorías para el combo
        return "productos/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("producto") ProductoA producto, RedirectAttributes redirectAttributes) {
        productoService.crearOEditar(producto);
        redirectAttributes.addFlashAttribute("msg", "Producto actualizado exitosamente!");
        return "redirect:/productos";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Integer id, Model model) {
        ProductoA producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
        return "productos/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        productoService.eliminarPorId(id);
        redirectAttributes.addFlashAttribute("msg", "Producto eliminado exitosamente!");
        return "redirect:/productos";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Integer id, Model model) {
        ProductoA producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
        return "productos/details";
    }
}
