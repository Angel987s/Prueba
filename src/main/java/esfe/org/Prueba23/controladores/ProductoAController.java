package esfe.org.Prueba23.controladores;

import esfe.org.Prueba23.modelos.EtiquetaA;
import esfe.org.Prueba23.modelos.ProductoA;
import esfe.org.Prueba23.servicios.interfaces.IProductoAService;
import esfe.org.Prueba23.servicios.interfaces.ICategoriaAService;
import esfe.org.Prueba23.servicios.implementaciones.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.NoSuchElementException;
import java.nio.file.Paths;
import java.nio.file.Files;


import org.springframework.core.io.Resource;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/productos")
public class ProductoAController {

    @Autowired
    private IProductoAService productoService;

    @Autowired
    private ICategoriaAService categoriaService;

    @Autowired
    private ImageStorageService imageStorageService;


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
    public String delPhone(@PathVariable("id") Long id, ProductoA producto, Model model) {
        producto.getEtiquetas().removeIf(elemento -> elemento.getId() == id);
        model.addAttribute("producto", producto);
        if (producto.getId() != null && producto.getId() > 0)
            return "productos/edit";
        else
            return "productos/create";
    }
    

@PostMapping("/save")
public String save(ProductoA producto, BindingResult result, Model model, @RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
  if (result.hasErrors()) {
         model.addAttribute("producto", producto);
         attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
         return "productos/create";
     }

     try {
         if (file != null && !file.isEmpty()) {
             UUID uuid = UUID.randomUUID();
             String newFileName = imageStorageService.storeImage(file, uuid.toString());
             producto.setUrlImage(newFileName);
         }

         if (producto.getEtiquetas() != null) {
             for (EtiquetaA item : producto.getEtiquetas()) {
                 if (item.getId() != null && item.getId() < 1) {
                     item.setId(null);
                 }
                 item.setProducto(producto);
             }
         }

         if (producto.getId() != null && producto.getId() > 0) {
             ProductoA productoUpdate = productoService.buscarPorId(producto.getId()).get();
             Map<Long, EtiquetaA> etiquetasData = new HashMap<>();

             if (productoUpdate.getEtiquetas() != null) {
                 for (EtiquetaA item : productoUpdate.getEtiquetas()) {
                     etiquetasData.put(item.getId(), item);
                 }
             }

             productoUpdate.setNombreA(producto.getNombreA());
             productoUpdate.setPrecioA(producto.getPrecioA());
             productoUpdate.setCategoriaA(producto.getCategoriaA());
             productoUpdate.setExistenciaA(producto.getExistenciaA());
             productoUpdate.setUrlImage(producto.getUrlImage());

             if (producto.getEtiquetas() != null) {
                 for (EtiquetaA item : producto.getEtiquetas()) {
                     if (item.getId() == null) {
                         if (productoUpdate.getEtiquetas() == null) {
                             productoUpdate.setEtiquetas(new ArrayList<>());
                         }
                         item.setProducto(productoUpdate);
                         productoUpdate.getEtiquetas().add(item);
                     } else {
                         if (etiquetasData.containsKey(item.getId())) {
                             EtiquetaA etiquetaAUpdate = etiquetasData.get(item.getId());
                             etiquetaAUpdate.setNombre(item.getNombre());
                             etiquetasData.remove(item.getId());
                         }
                     }
                 }
             }

             if (!etiquetasData.isEmpty()) {
                 for (Map.Entry<Long, EtiquetaA> entry : etiquetasData.entrySet()) {
                     productoUpdate.getEtiquetas().removeIf(elemento -> elemento.getId().equals(entry.getKey()));
                 }
             }

             producto = productoUpdate;
         }

         productoService.crearOEditar(producto);
         attributes.addFlashAttribute("msg", "Producto creado o actualizado correctamente");

     } catch (IOException e) {
         attributes.addFlashAttribute("error", "Error al guardar la imagen.");
         return "productos/create";
     }

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
public String update(@ModelAttribute("producto") ProductoA producto, BindingResult result, Model model, MultipartFile file, RedirectAttributes attributes) throws IOException {
    if (result.hasErrors()) {
        model.addAttribute("producto", producto);
        attributes.addFlashAttribute("error", "No se pudo actualizar debido a un error.");
        return "productos/edit";
    }

    ProductoA productoActual = productoService.buscarPorId(producto.getId()).get();

    // Verificar si hay una nueva imagen y eliminar la anterior
    if (file != null && !file.isEmpty()) {
        // Eliminar la imagen anterior si existe
        if (productoActual.getUrlImage() != null && !productoActual.getUrlImage().isEmpty()) {
            imageStorageService.deleteImage(productoActual.getUrlImage());
        }

        // Guardar la nueva imagen
        UUID uuid = UUID.randomUUID();
        producto.setUrlImage(imageStorageService.storeImage(file, uuid.toString()));
    } else {
        // Mantener la URL de la imagen anterior si no se ha cargado una nueva
        producto.setUrlImage(productoActual.getUrlImage());
    }

    productoService.crearOEditar(producto);
    attributes.addFlashAttribute("msg", "Producto actualizado exitosamente!");
    return "redirect:/productos";
}



    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Integer id, Model model) {
        ProductoA producto = productoService.buscarPorId(id).get();
        model.addAttribute("producto", producto);
        return "productos/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(ProductoA producto, RedirectAttributes attributes) throws IOException {
    if (producto.getUrlImage() != null && producto.getUrlImage().trim().length() > 0) {
        imageStorageService.deleteImage(producto.getUrlImage());
    }

    productoService.eliminarPorId(producto.getId());

    attributes.addFlashAttribute("msg", "Producto eliminado correctamente");
    
    return "redirect:/productos";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        ProductoA producto = productoService.buscarPorId(id).get();
        model.addAttribute("producto", producto);
        return "productos/details";
    }


/* 
    @GetMapping("/images/{id}")
    public ResponseEntity<Resource> viewImage(@PathVariable Integer id) {
        try {
            ProductoA producto = productoService.buscarPorId(id).orElseThrow(() -> new NoSuchElementException("Producto no encontrado"));
            Resource resource = imageStorageService.loadImageAsResource(producto.getUrlImage());
    
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // o MediaType.IMAGE_PNG según el tipo de imagen
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            // Manejo de error si la imagen no se puede cargar
            return ResponseEntity.notFound().build();
        }
    }
*/

    @GetMapping("/images/{id}")
    public ResponseEntity<Resource> viewImage(@PathVariable Integer id) {
        try {
            ProductoA productoA = productoService.buscarPorId(id).get();
            Resource resource = imageStorageService.loadImageAsResource(productoA.getUrlImage());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // o MediaType.IMAGE_PNG según el tipo de imagen
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            // Manejo de error si la imagen no se puede cargar
            return ResponseEntity.notFound().build();
        }
    }
    

    
}
