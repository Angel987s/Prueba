package esfe.org.Prueba23.servicios.implementaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import esfe.org.Prueba23.modelos.Marca;
import esfe.org.Prueba23.repositorios.IMarcaRepository;
import esfe.org.Prueba23.servicios.interfaces.IMarcaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaService implements IMarcaService {
    
    @Autowired
    private IMarcaRepository marcaRepository;

    @Override
    public List<Marca> obtenerTodos() {
        return marcaRepository.findAll();
    }

    @Override
    public Optional<Marca> buscarPorId(Long id) {
        return marcaRepository.findById(id);
    }

    @Override
    public Marca crearOEditar(Marca marca) {
        return marcaRepository.save(marca);
    }

    @Override
    public void eliminarPorId(Long id) {
        marcaRepository.deleteById(id);
    }

    @Override
    public Page<Marca> buscarTodosPaginados(Pageable pageable) {
        return marcaRepository.findAll(pageable);
    }
}
