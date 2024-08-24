package esfe.org.Prueba23.servicios.interfaces;

import esfe.org.Prueba23.modelos.Marca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IMarcaService {
    Page<Marca> buscarTodosPaginados(Pageable pageable);

    List<Marca> obtenerTodos();

    Optional<Marca> buscarPorId(Long id);

    Marca crearOEditar(Marca marca);

    void eliminarPorId(Long id);

}
