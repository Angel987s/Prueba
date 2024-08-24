package esfe.org.Prueba23.servicios.implementaciones;

import esfe.org.Prueba23.modelos.Producto;
import esfe.org.Prueba23.repositorios.IProductoRepository;
import esfe.org.Prueba23.servicios.interfaces.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private IProductoRepository productoRepository;

    @Override
    public Page<Producto> buscarTodosPaginados(Pageable pageable) {
        return productoRepository.findAll(pageable);
    }

    @Override
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto buscarPorId(Long id) {
        Optional<Producto> detalleOrdenOptional = productoRepository.findById(id);
        if (detalleOrdenOptional.isPresent()) {
            return detalleOrdenOptional.get();
        } else {
            throw new RuntimeException("Detalle de Orden no encontrado con ID: " + id); // O lanzar una excepción personalizada
        }
    }

    @Override
    public Producto crearOEditar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void eliminarPorId(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Detalle de Orden no encontrado con ID: " + id); // O lanzar una excepción personalizada
        }
    }
  
}