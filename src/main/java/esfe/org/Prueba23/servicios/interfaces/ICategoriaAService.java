package esfe.org.Prueba23.servicios.interfaces;

import esfe.org.Prueba23.modelos.CategoriaA;

import java.util.List;
import java.util.Optional;

public interface ICategoriaAService {
    List<CategoriaA> listarTodas();
    Optional<CategoriaA> obtenerPorId(Integer id);
    CategoriaA guardar(CategoriaA categoriaA);
    void eliminar(Integer id);
}
