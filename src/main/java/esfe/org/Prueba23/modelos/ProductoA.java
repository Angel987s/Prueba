package esfe.org.Prueba23.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class ProductoA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String nombreA;

    @NotNull
    @Positive
    private Double precioA;

    @Positive
    private Integer existenciaA;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaA categoriaA;

    // Getters y Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreA() {
        return nombreA;
    }

    public void setNombreA(String nombreA) {
        this.nombreA = nombreA;
    }

    public Double getPrecioA() {
        return precioA;
    }

    public void setPrecioA(Double precioA) {
        this.precioA = precioA;
    }

    public Integer getExistenciaA() {
        return existenciaA;
    }

    public void setExistenciaA(Integer existenciaA) {
        this.existenciaA = existenciaA;
    }

    public CategoriaA getCategoriaA() {
        return categoriaA;
    }

    public void setCategoriaA(CategoriaA categoriaA) {
        this.categoriaA = categoriaA;
    }
}
