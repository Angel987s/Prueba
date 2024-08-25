package esfe.org.Prueba23.servicios.implementaciones;

import esfe.org.Prueba23.modelos.CategoriaA;
import esfe.org.Prueba23.repositorios.ICategoriaRepository;
import esfe.org.Prueba23.servicios.interfaces.ICategoriaAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaAService implements ICategoriaAService {

    @Autowired
    private ICategoriaRepository categoriaARepository;

    @Override
    public List<CategoriaA> listarTodas() {
        return categoriaARepository.findAll();
    }

    @Override
    public Optional<CategoriaA> obtenerPorId(Integer id) {
        return categoriaARepository.findById(id);
    }

    @Override
    public CategoriaA guardar(CategoriaA categoriaA) {
        return categoriaARepository.save(categoriaA);
    }

    @Override
    public void eliminar(Integer id) {
        categoriaARepository.deleteById(id);
    }
}
