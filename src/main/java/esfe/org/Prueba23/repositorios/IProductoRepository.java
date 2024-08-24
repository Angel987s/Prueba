package esfe.org.Prueba23.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import esfe.org.Prueba23.modelos.Producto;

public interface IProductoRepository extends JpaRepository<Producto, Long> {

}
