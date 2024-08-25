package esfe.org.Prueba23.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "etiquetas_a")
public class EtiquetaA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la etiqueta es requerido")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private ProductoA producto;

    // Getters y Setters

    public EtiquetaA() {
    }
    public EtiquetaA(ProductoA producto, String nombre) {
        this.producto = producto;
        this.nombre = nombre;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ProductoA getProducto() {
        return producto;
    }

    public void setProducto(ProductoA producto) {
        this.producto = producto;
    }
}
