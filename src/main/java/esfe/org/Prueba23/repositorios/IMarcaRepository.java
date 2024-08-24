package esfe.org.Prueba23.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import esfe.org.Prueba23.modelos.Marca;


public interface IMarcaRepository extends JpaRepository<Marca, Long> {

}
