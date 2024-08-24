package esfe.org.Prueba23.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre de la Nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El nombre de la Precio es obligatorio")

    private Double precio; 

    @NotNull(message = "El nombre de la descripcion es obligatorio")

    private String descripcion;

    @NotNull(message = "El nombre de la Marca es obligatorio")

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;

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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

}
