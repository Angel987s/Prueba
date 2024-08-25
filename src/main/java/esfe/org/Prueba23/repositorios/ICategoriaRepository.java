package esfe.org.Prueba23.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import esfe.org.Prueba23.modelos.CategoriaA;

public interface ICategoriaRepository extends JpaRepository<CategoriaA, Integer> {
}