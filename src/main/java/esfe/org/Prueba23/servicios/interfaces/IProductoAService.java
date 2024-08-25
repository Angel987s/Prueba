package esfe.org.Prueba23.servicios.interfaces;

import esfe.org.Prueba23.modelos.ProductoA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProductoAService {
    Page<ProductoA> buscarTodosPaginados(Pageable pageable);

    List<ProductoA> obtenerTodos();

    Optional<ProductoA> buscarPorId(Integer id);

    ProductoA crearOEditar(ProductoA productoA);

    void eliminarPorId(Integer id);
}
