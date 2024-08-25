package esfe.org.Prueba23.repositorios;

import esfe.org.Prueba23.modelos.ProductoA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoARepository extends JpaRepository<ProductoA, Integer> {
}