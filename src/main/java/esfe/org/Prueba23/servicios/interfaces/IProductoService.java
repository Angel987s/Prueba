package esfe.org.Prueba23.servicios.interfaces;

import esfe.org.Prueba23.modelos.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductoService {
    Page<Producto> buscarTodosPaginados(Pageable pageable);

    List<Producto> obtenerTodos();

    Producto buscarPorId(Long id);

    Producto crearOEditar(Producto producto);

    void eliminarPorId(Long id);

}