package esfe.org.Prueba23.modelos;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "marcas")
public class Marca {
 @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  

    private Long id;
    @NotNull(message = "El nombre de la marca es obligatorio")

    private String nombre;

    @NotNull(message = "El nombre de la descripcion es obligatorio")

    private String descripcion;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
