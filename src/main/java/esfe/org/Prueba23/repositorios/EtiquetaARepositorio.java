package esfe.org.Prueba23.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import esfe.org.Prueba23.modelos.EtiquetaA;

@Repository
public interface EtiquetaARepositorio extends JpaRepository<EtiquetaA, Long> {
}
