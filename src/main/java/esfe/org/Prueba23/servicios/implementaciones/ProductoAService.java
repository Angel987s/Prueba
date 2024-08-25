package esfe.org.Prueba23.servicios.implementaciones;

import esfe.org.Prueba23.modelos.ProductoA;
import esfe.org.Prueba23.repositorios.IProductoARepository;
import esfe.org.Prueba23.servicios.interfaces.IProductoAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoAService implements IProductoAService {

    @Autowired
    private IProductoARepository productoARepository;

    @Override
    public Page<ProductoA> buscarTodosPaginados(Pageable pageable) {
        return productoARepository.findAll(pageable);
    }

    @Override
    public List<ProductoA> obtenerTodos() {
        return productoARepository.findAll();
    }

    @Override
    public ProductoA buscarPorId(Integer id) {
        Optional<ProductoA> productoAOptional = productoARepository.findById(id);
        if (productoAOptional.isPresent()) {
            return productoAOptional.get();
        } else {
            throw new RuntimeException("Producto no encontrado con ID: " + id); // O lanzar una excepción personalizada
        }
    }

    @Override
    public ProductoA crearOEditar(ProductoA productoA) {
        return productoARepository.save(productoA);
    }

    @Override
    public void eliminarPorId(Integer id) {
        if (productoARepository.existsById(id)) {
            productoARepository.deleteById(id);
        } else {
            throw new RuntimeException("Producto no encontrado con ID: " + id); // O lanzar una excepción personalizada
        }
    }
}
