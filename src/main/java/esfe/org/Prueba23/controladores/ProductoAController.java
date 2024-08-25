package esfe.org.Prueba23.controladores;

import esfe.org.Prueba23.modelos.EtiquetaA;
import esfe.org.Prueba23.modelos.ProductoA;
import esfe.org.Prueba23.servicios.interfaces.IProductoAService;
import esfe.org.Prueba23.servicios.interfaces.ICategoriaAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        model.addAttribute("categorias", categoriaService.listarTodas()); 
        
        return "productos/create";
    }


    @PostMapping("/addtelefonos")
    public String addPhone(ProductoA alumno, Model model) {
        if (alumno.getEtiquetas() == null) {
            alumno.setEtiquetas(new ArrayList<>());
        }
        
        alumno.getEtiquetas().add(new EtiquetaA(alumno, ""));
    
        if (alumno.getEtiquetas() != null) {
            Long idDet = 0L;
            for (EtiquetaA item : alumno.getEtiquetas()) {
                if (item.getId() == null || item.getId() < 1) {
                    idDet--;
                    item.setId(idDet);
                }
            }
        }
    
        model.addAttribute("producto", alumno);
        model.addAttribute("categorias", categoriaService.listarTodas()); 
        if (alumno.getId() != null && alumno.getId() > 0) {
            return "productos/edit";
        } else {
            return "productos/create";
        }
    }


    @PostMapping("/deltelefonos/{id}")
    public String delPhone(@PathVariable("id") Long id, ProductoA alumno, Model model) {
        alumno.getEtiquetas().removeIf(elemento -> elemento.getId() == id);
        model.addAttribute(alumno);
        if (alumno.getId() != null && alumno.getId() > 0)
            return "productos/edit";
        else
            return "productos/create";
    }
    

    @PostMapping("/save")
public String save(ProductoA producto, BindingResult result, Model model, RedirectAttributes attributes) {
    if (result.hasErrors()) {
        model.addAttribute("producto", producto);
        attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
        return "productos/create";
    }

    if (producto.getEtiquetas() != null) {
        for (EtiquetaA item : producto.getEtiquetas()) {
            if (item.getId() != null && item.getId() < 1)
                item.setId(null);
            item.setProducto(producto);
        }
    }

    if (producto.getId() != null && producto.getId() > 0) {
        // Funcionalidad para cuando es modificar un registro
        ProductoA productoUpdate = productoService.buscarPorId(producto.getId()).get();
        // Almacenar en un diccionario las etiquetas que están
        // guardadas en la base de datos para mejor acceso a ellas
        Map<Long, EtiquetaA> etiquetasData = new HashMap<>();
        if (productoUpdate.getEtiquetas() != null) {
            for (EtiquetaA item : productoUpdate.getEtiquetas()) {
                etiquetasData.put(item.getId(), item);
            }
        }
        // Actualizar los registros que vienen de la vista hacia el que se encuentra por id
        productoUpdate.setNombreA(producto.getNombreA());
        productoUpdate.setPrecioA(producto.getPrecioA());
        productoUpdate.setCategoriaA(producto.getCategoriaA());

        // Recorrer las etiquetas obtenidas desde la vista y actualizar
        // productoUpdate para que implemente los cambios
        if (producto.getEtiquetas() != null) {
            for (EtiquetaA item : producto.getEtiquetas()) {
                if (item.getId() == null) {
                    if (productoUpdate.getEtiquetas() == null)
                        productoUpdate.setEtiquetas(new ArrayList<>());
                    item.setProducto(productoUpdate);
                    productoUpdate.getEtiquetas().add(item);
                } else {
                    if (etiquetasData.containsKey(item.getId())) {
                        EtiquetaA etiquetaAUpdate = etiquetasData.get(item.getId());
                        // Actualizar las propiedades de EtiquetaA
                        etiquetaAUpdate.setNombre(item.getNombre());
                        // Remover del diccionario las etiquetas que no vienen desde la vista
                        etiquetasData.remove(item.getId());
                    }
                }
            }
        }
        // Eliminar las etiquetas que ya no están en la vista
        if (!etiquetasData.isEmpty()) {
            for (Map.Entry<Long, EtiquetaA> entry : etiquetasData.entrySet()) {
                productoUpdate.getEtiquetas().removeIf(elemento -> elemento.getId().equals(entry.getKey()));
            }
        }
        producto = productoUpdate;
    }
    productoService.crearOEditar(producto);
    attributes.addFlashAttribute("msg", "Producto creado o actualizado correctamente");
    return "redirect:/productos";
}


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        ProductoA producto = productoService.buscarPorId(id).get();
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
        ProductoA producto = productoService.buscarPorId(id).get();
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
    public String details(@PathVariable("id") Integer id, Model model) {
        ProductoA producto = productoService.buscarPorId(id).get();
        model.addAttribute("producto", producto);
        return "productos/details";
    }
    
}
